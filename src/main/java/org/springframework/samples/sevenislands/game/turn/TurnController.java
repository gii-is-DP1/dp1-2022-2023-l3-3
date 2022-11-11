package org.springframework.samples.sevenislands.game.turn;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurnController {

    private static final String VIEWS_GAME_ASIGN_TURN = "games/asignTurn"; // vista para decidir turnos
    private static final String VIEWS_PASIVE_GAME = "games/pasiveGame"; // vista pasiva del juego
    private static final String VIEWS_ACTIVE_GAME = "games/activeGame"; // vista activa del juego

    private final TurnService turnService;
    private final PlayerService playerService;

    @Autowired
    public TurnController(TurnService turnService, PlayerService playerService) {
        this.turnService = turnService;
        this.playerService = playerService;
    }

    @GetMapping("/game/turn")
    public String GameTurn(Principal principal) { // vista del botón rojo
        return VIEWS_GAME_ASIGN_TURN;
    }

    @GetMapping("/game/AsignTurn")
    public String GameAsignTurn(Principal principal) { // ordenación de jugadores según botón rojo
        Player player = playerService.findPlayersByName(principal.getName());
        Turn turn = new Turn();
        turn.setTimePress(LocalDateTime.now());
        turn.setPlayer(player);
        turnService.save(turn);

        return "redirect:/game";

    }

}
