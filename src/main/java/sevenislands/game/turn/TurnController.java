package sevenislands.game.turn;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TurnController {

    private static final String VIEWS_GAME = "game/game"; // vista pasiva del juego

    private final TurnService turnService;
    private final UserService userService;
    private final RoundService roundService;
    private final LobbyService lobbyService;
    private final GameService gameService;

    @Autowired
    public TurnController(GameService gameService, LobbyService lobbyService, RoundService roundService, TurnService turnService, UserService userService) {
        this.turnService = turnService;
        this.userService = userService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
    }

    @GetMapping("/turn")
    public String gameTurn(ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
        response.addHeader("Refresh", "1");

        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
        Round round = roundList.get(roundList.size()-1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn turn = turnList.get(turnList.size()-1);
        
        model.put("player", logedUser);
        model.put("player_turn", turn.getUser());
        model.put("dice", turn.getDice());
    
        Duration timeElapsed = Duration.between(turn.getStartTime(), LocalDateTime.now());
        model.put("time_left", 40-timeElapsed.toSeconds());
        if(turn.getUser().getId()==logedUser.getId() && timeElapsed.toSeconds()>=40) {
            return "redirect:/turn/endTurn";
        }

        return VIEWS_GAME;
    }

    @GetMapping("/turn/endTurn")
    public String gameEndTurn(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request) throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
        
        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
        Round round = roundList.get(roundList.size()-1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size()-1);
        Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
        List<User> userList = lobby.getUsers();

        if(logedUser.getId()==lastTurn.getUser().getId()) {
            if(turnList.size()>=userList.size()) return "redirect:/turn/newRound";
            Turn turn = new Turn();
            Integer nextUser = (userList.indexOf(logedUser)+1)%userList.size();
            turn.setStartTime(LocalDateTime.now());
            turn.setRound(round);
            turn.setUser(userList.get(nextUser));
            turnService.save(turn);
        } 
        return "redirect:/turn";
    }

    @GetMapping("/turn/dice")
    public String gameRollDice(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request) throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
        
        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
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
    public String gameAsignTurn(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request) throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
        if(game.isPresent()) {
            Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
            List<User> userList = lobby.getUsers();
            List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
    
            Round round = new Round();
            Turn turn = new Turn();
    
            round.setGame(game.get());
            turn.setRound(round);
            turn.setStartTime(LocalDateTime.now());
            if(roundService.findRoundsByGameId(game.get().getId()).isEmpty()) {
                turn.setUser(logedUser);
            } else if (turnService.findByRoundId(roundList.get(roundList.size()-1).getId()).size() >= userList.size()) { 
                Integer nextUser = (userList.indexOf(logedUser)+1)%userList.size();
                turn.setUser(userList.get(nextUser));
            } else return "redirect:/turn";
    
            roundService.save(round);
            turnService.save(turn);
            return "redirect:/turn";
        } else return "redirect:/home";
    }

}
