package sevenislands.game.turn;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.Game;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.tools.checkers;
import sevenislands.tools.entityAssistant;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurnController {

    private static final String VIEWS_GAME = "game/game"; // vista pasiva del juego

    private final TurnService turnService;
    private final UserService userService;
    private final RoundService roundService;
    private final LobbyService lobbyService;

    @Autowired
    public TurnController(LobbyService lobbyService, RoundService roundService, TurnService turnService, UserService userService) {
        this.turnService = turnService;
        this.userService = userService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
    }

    @GetMapping("/turn")
    public String gameTurn(Map<String, Object> model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        response.addHeader("Refresh", "1");

        User user = userService.findUser(principal.getName());
        Optional<Game> game = entityAssistant.getGameOfPlayer(request);
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
        Round round = roundList.get(roundList.size()-1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn turn = turnList.get(turnList.size()-1);
        
      

        model.put("player", userService.findUser(principal.getName()));
        model.put("player_turn", turn.getUser());
        model.put("dice", turn.getDice());
    
        Duration timeElapsed = Duration.between(turn.getStartTime(), LocalDateTime.now());
        model.put("time_left", 40-timeElapsed.toSeconds());
        if(turn.getUser().getId()==user.getId() && timeElapsed.toSeconds()>=40) {
            return "redirect:/turn/endTurn";
        }

        return VIEWS_GAME;
    }

    @GetMapping("/turn/endTurn")
    public String gameEndTurn(Principal principal, HttpServletRequest request) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        
        Optional<Game> game = entityAssistant.getGameOfPlayer(request);
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
        Round round = roundList.get(roundList.size()-1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size()-1);
        User user = userService.findUser(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
        List<User> userList = lobby.getUsers();

        if(user.getId()==lastTurn.getUser().getId()) {
            if(turnList.size()>=userList.size()) return "redirect:/turn/newRound";
            Turn turn = new Turn();
            Integer nextUser = (userList.indexOf(user)+1)%userList.size();
            turn.setStartTime(LocalDateTime.now());
            turn.setRound(round);
            turn.setUser(userList.get(nextUser));
            turnService.save(turn);
        } 
        return "redirect:/turn";
    }

    @GetMapping("/turn/dice")
    public String gameRollDice(HttpServletRequest request) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        
        Optional<Game> game = entityAssistant.getGameOfPlayer(request);
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
        Round round = roundList.get(roundList.size()-1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size()-1);
        Random randomGenerator = new Random();
        Integer dice = randomGenerator.nextInt(6)+1;
        lastTurn.setDice(dice);
        turnService.save(lastTurn);
        return "redirect:/turn";
    }

    @GetMapping("/turn/newRound")
    public String gameAsignTurn(Principal principal, HttpServletRequest request) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        //TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
        User user = userService.findUser(principal.getName());
        //TODO: Poner el Game como Optional<Game> y realizar la comprobación de que existe
        Game game = entityAssistant.getGameOfPlayer(request).get();
        //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
        Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
        List<User> userList = lobby.getUsers();
        List<Round> roundList = roundService.findRoundsByGameId(game.getId()).stream().collect(Collectors.toList());

        Round round = new Round();
        Turn turn = new Turn();

        round.setGame(game);
        turn.setRound(round);
        turn.setStartTime(LocalDateTime.now());
        if(roundService.findRoundsByGameId(game.getId()).isEmpty()) {
            turn.setUser(user);
        } else if (turnService.findByRoundId(roundList.get(roundList.size()-1).getId()).size() >= userList.size()) {  //Quizás podríamos poner esta condición
            Integer nextUser = (userList.indexOf(user)+1)%userList.size();                      //en un método en caso de que lo vayamos
            turn.setUser(userList.get(nextUser));                                                 //a tener que comprobar en varios sitios
        } else return "redirect:/turn";

        roundService.save(round);
        turnService.save(turn);
        return "redirect:/turn";
    }

}
