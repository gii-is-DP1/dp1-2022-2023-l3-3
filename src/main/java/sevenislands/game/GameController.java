package sevenislands.game;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.turn.TurnService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class GameController {

    private static final String VIEWS_GAME_ASIGN_TURN = "game/asignTurn"; // vista para decidir turnos
    private static final String VIEW_WELCOME = "redirect:/"; // vista para welcome
    private static final String VIEWS_HOME =  "redirect:/home"; // vista para home
    private static final String VIEWS_TURN =  "redirect:/turn"; // vista para turn
    private static final String VIEWS_LOBBY =  "redirect:/lobby"; // vista para lobby
    private static final String VIEWS_FINISHED_GAMES = "list/finishedGames"; //vista de partidas finalizadas
    private static final String VIEWS_INPROGRESS_GAMES = "list/inProgressGames"; //vista de partidas en curso
    private static final String VIEWS_GAMES_AS_PLAYER = "list/gamesAsPlayer"; //vista de partidas jugadas


    

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;
    private final TurnService turnService;

    @Autowired
    public GameController(UserService userService, GameService gameService, LobbyService lobbyService, TurnService turnService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.turnService = turnService;
    }

    @GetMapping("/game")
    public String createGame(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) throws Exception {
        if(userService.checkUserNoExists(request)) return VIEW_WELCOME;
        if(lobbyService.checkUserNoLobby(logedUser)) return VIEWS_HOME;
        if(lobbyService.checkLobbyNoAllPlayers(logedUser)) return VIEWS_LOBBY;
        if(gameService.checkUserGameWithRounds(logedUser)) return VIEWS_TURN;
        response.addHeader("Refresh", "5");
       try {
        Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
        if(gameService.findGameByNickname(logedUser.getNickname(), true).isEmpty()) {
            gameService.initGame(lobby);
            lobbyService.disableLobby(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
       } catch (Exception e) {
        return VIEWS_HOME;
       }
    }

    @GetMapping("/endGame")
    public String endGame(ModelMap model, @ModelAttribute("logedUser") User logedUser){
        User winner = turnService.findWinner(logedUser);
        model.put("winner", winner);
        gameService.endGame(logedUser);
        return"game/endgame";
    }

    @GetMapping("/game/finished")
    public String listFinishedGames(ModelMap model) {
        List<Game> games = this.gameService.findGameActive(false);
        model.put("games", games);
        return VIEWS_FINISHED_GAMES;
    }

    @GetMapping("/game/InProgress")
    public String listGames(ModelMap model) {
        List<Game> games = this.gameService.findGameActive(true);
        model.put("games", games);
        return VIEWS_INPROGRESS_GAMES;
    }

    @GetMapping("/game/gamesAsPlayer")
    public String listGamesAsPlayer(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Game> games = gameService.findGameByNickname("player4", false).stream().collect(Collectors.toList());
        model.put("games", games);
        return VIEWS_GAMES_AS_PLAYER;
    }
}
