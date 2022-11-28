package sevenislands.game;

import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GameController {

    private static final String VIEWS_GAME_ASIGN_TURN = "game/asignTurn"; // vista para decidir turnos

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
    public String createGame(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
        if(gameService.checkUserGameWithRounds(logedUser)) return "redirect:/turn";
        response.addHeader("Refresh", "5");
        Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
        if(!gameService.findGamebByLobbyId(lobby.getId()).isPresent()) {
            Game game = new Game();
            game.setCreationDate(new Date(System.currentTimeMillis()));
            game.setLobby(lobby);
            gameService.save(game);
            lobby.setActive(false);
            lobbyService.save(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
    }
}
