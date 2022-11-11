package org.springframework.samples.sevenislands.game;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.card.CardService;
import org.springframework.samples.sevenislands.game.island.IslandService;
import org.springframework.samples.sevenislands.game.round.RoundService;
import org.springframework.samples.sevenislands.game.turn.Turn;
import org.springframework.samples.sevenislands.game.turn.TurnService;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.lobby.LobbyService;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

    private static final String VIEWS_GAME_ASIGN_TURN = "games/asignTurn"; // vista para decidir turnos
    private static final String VIEWS_PASIVE_GAME = "games/pasiveGame"; // vista pasiva del juego
    private static final String VIEWS_ACTIVE_GAME = "games/activeGame"; // vista activa del juego

    private final GameService gameService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;

    @Autowired
    public GameController(GameService gameService,
            PlayerService playerService,
            LobbyService lobbyService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
    }

    ////////////////////////////////////////////////////////////////////////////
    /// MÉTODOS PARA LA CREACIÓN DEL JUEGO
    ////////////////////////////////////////////////////////////////////////////

    @GetMapping("/game")
    public ModelAndView Game(Principal principal) {
        ModelAndView result = new ModelAndView(VIEWS_PASIVE_GAME);
        return result;
    }

    @GetMapping("/game/create")
    public String createGame(Principal principal) {
        Game game = new Game();
        Player player = playerService.findPlayersByName(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        gameService.save(game);
        lobby.setGame(game);
        lobbyService.update(lobby);
        return "redirect:/game/turn";
    }
}
