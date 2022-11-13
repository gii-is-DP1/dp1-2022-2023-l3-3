package org.springframework.samples.sevenislands.game.turn;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.card.board.Board;
import org.springframework.samples.sevenislands.card.board.BoardService;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.lobby.LobbyService;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurnController {

    private static final String VIEWS_GAME = "games/game"; // vista pasiva del juego

    private final TurnService turnService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;
    private final BoardService boardService;

    @Autowired
    public TurnController(TurnService turnService, PlayerService playerService, LobbyService lobbyService,
            BoardService boardService) {
        this.turnService = turnService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.boardService = boardService;
    }

    @GetMapping("/turn")
    public String gameTurn(Principal principal, HttpServletResponse response, Map<String, Object> model) {
        // response.addHeader("Refresh", "2");
        Player player = playerService.findPlayersByName(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        model.put("players", lobby.getPlayers());
        model.put("board", boardService.findById(1).get());
        return VIEWS_GAME;
    }

    @GetMapping("/turn/asignTurn")
    public String gameAsignTurn(Principal principal) { // ordenación de jugadores según botón rojo
        Player player = playerService.findPlayersByName(principal.getName());
        // RONDA 0 --> ASIGNAR CARTAS DE INICIO
        Turn turn = new Turn();
        turn.setTimePress(LocalDateTime.now());
        turn.setPlayer(player);
        turnService.save(turn);
        return "redirect:/turn";

    }

    @GetMapping("/turn/rollDice")
    public String rollDice(Principal principal) {
        Player player = playerService.findPlayersByName(principal.getName());
        Turn turn = new Turn();
        turn.setPlayer(player);

        Integer num = Turn.rollDice();
        turn.setDice(num);
        turnService.save(turn);
        return "redirect:/game";
    }
}
