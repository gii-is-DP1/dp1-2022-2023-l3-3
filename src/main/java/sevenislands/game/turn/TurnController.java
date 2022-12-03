package sevenislands.game.turn;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
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
    private final IslandService islandService;

    @Autowired
    public TurnController(GameService gameService, LobbyService lobbyService, RoundService roundService, TurnService turnService, IslandService islandService, UserService userService) {
        this.turnService = turnService;
        this.userService = userService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.islandService = islandService;
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
        Turn lastTurn = turnList.get(turnList.size()-1);
        List<Island> islandList = islandService.findIslandsByGameId(game.get().getId());
        
        model.put("player", logedUser);
        model.put("player_turn", lastTurn.getUser());
        model.put("dice", lastTurn.getDice());
        model.put("islandList", islandList);
    
        Duration timeElapsed = Duration.between(lastTurn.getStartTime(), LocalDateTime.now());
        model.put("time_left", 40-timeElapsed.toSeconds());
        if(lastTurn.getUser().getId()==logedUser.getId() && timeElapsed.toSeconds()>=40) {
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
            turnService.initTurn(logedUser, round, userList, null);
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
        turnService.dice(lastTurn);
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
    
            turnService.assignTurn(logedUser, game, userList, roundList);
    
            return "redirect:/turn";
        } else return "redirect:/home";
    }

}
