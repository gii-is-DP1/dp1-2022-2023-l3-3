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

    private static final String VIEWS_GAMETURN = "games/gameTurn";
    private static final String VIEWS_GAME = "games/game";

    private final GameService gameService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;
    private final RoundService roundService;
    private final TurnService turnService;
    private final IslandService islandService;
    private final CardService cardService;

    @Autowired
    public GameController(GameService gameService,
            PlayerService playerService,
            LobbyService lobbyService,
            RoundService roundService,
            TurnService turnService,
            IslandService islandService,
            CardService cardService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.roundService = roundService;
        this.turnService = turnService;
        this.islandService = islandService;
        this.cardService = cardService;
    }

    ////////////////////////////////////////////////////////////////////////////
    /// MÉTODOS PARA LA CREACIÓN DEL JUEGO ///
    ////////////////////////////////////////////////////////////////////////////

    @GetMapping("/game")
    public ModelAndView Game(Principal principal) {
        ModelAndView result = new ModelAndView(VIEWS_GAME);
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

    ////////////////////////////////////////////////////////////////////////////
    /// MÉTODOS PARA LA ASIGNACIÓN DE TURNOS ///
    ////////////////////////////////////////////////////////////////////////////

    @GetMapping("/game/AsignTurn")
    public String GameAsignTurn(Principal principal) {
        Player player = playerService.findPlayersByName(principal.getName());
        Turn turn = new Turn();
        turn.setTimePress(LocalDateTime.now());
        turn.setPlayer(player);
        turnService.save(turn);

        return "redirect:/game";

    }

    @GetMapping("/game/turn")
    public String GameTurn(Principal principal) {

        return VIEWS_GAMETURN;
    }

    ////////////////////////////////////////////////////////////////////////////
    /// MÉTODOS PARA ACCIONES DEL JUEGO ///
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Representa una tirada del dado
     * 
     * @param principal
     * @return ruta a la que vamos
     */
    public String throwingDie(Principal principal) {
        Player player = playerService.findPlayersByName(principal.getName());

    }
}
