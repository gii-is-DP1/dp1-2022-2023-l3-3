package sevenislands.game;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String VIEWS_FINISHED_GAMES = "list/listGames"; //vista de partidas finalizadas

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;

    @Autowired
    public GameController(UserService userService, GameService gameService, LobbyService lobbyService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
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
        if(gameService.findGameByNickname(logedUser.getNickname()).isEmpty()) {
            gameService.initGame(lobby);
            lobbyService.disableLobby(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
       } catch (Exception e) {
        return VIEWS_HOME;
       }
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
        return VIEWS_FINISHED_GAMES;
    }
}
