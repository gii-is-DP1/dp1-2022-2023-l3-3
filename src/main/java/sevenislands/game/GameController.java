package sevenislands.game;

import java.security.Principal;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.checkers;
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
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        if(checkers.checkUserNoGame(request)) return "redirect:/turn";
        response.addHeader("Refresh", "1");

        //TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
        Player player = playerService.findPlayer(principal.getName());
        //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId()).get();
        if(!gameService.findGamebByLobbyId(lobby.getId()).isPresent()) {
            Game game = new Game();
            game.setCreationDate(new Date(System.currentTimeMillis()));
            game.setLobby(lobby);
            gameService.save(game);
            lobby.setActive(false);
            lobbyService.update(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
    }
}
