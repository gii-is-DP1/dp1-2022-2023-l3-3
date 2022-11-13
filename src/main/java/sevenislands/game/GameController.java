package sevenislands.game;

import java.security.Principal;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.methods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    private static final String VIEWS_GAME_ASIGN_TURN = "game/asignTurn"; // vista para decidir turnos

    private final GameService gameService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, LobbyService lobbyService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
    }

    @GetMapping("/game")
    public String createGame(HttpServletRequest request, Principal principal, HttpServletResponse response) throws ServletException {
        if(methods.checkUserNoExists(request)) return "redirect:/";
        if(methods.checkUserNoLobby(request)) return "redirect:/home";
        if(methods.checkUserNoGame(request)) return "redirect:/turn";
        response.addHeader("Refresh", "1");

        Player player = playerService.findPlayer(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        if(gameService.findGamebByLobbyId(lobby.getId())==null) {
            Game game = new Game();
            game.setCreationDate(LocalDateTime.now());
            game.setLobby(lobby);
            gameService.save(game);
            lobby.setActive(false);
            lobbyService.update(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
    }
}