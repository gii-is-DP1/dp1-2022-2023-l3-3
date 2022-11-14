package org.springframework.samples.sevenislands.game;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.card.Card;
import org.springframework.samples.sevenislands.card.CardService;
import org.springframework.samples.sevenislands.game.island.Island;
import org.springframework.samples.sevenislands.game.island.IslandService;
import org.springframework.samples.sevenislands.game.round.Round;
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

    private final GameService gameService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;
    private final RoundService roundService;
    private final TurnService turnService;
    private final CardService cardService;
    private final IslandService islandService;

    @Autowired
    public GameController(GameService gameService,
            PlayerService playerService,
            LobbyService lobbyService,
            RoundService roundService,
            TurnService turnService,
            CardService cardService,
            IslandService islandService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.roundService = roundService;
        this.turnService = turnService;
        this.cardService = cardService;
        this.islandService = islandService;
    }

    ////////////////////////////////////////////////////////////////////////////
    /// MÉTODOS PARA LA CREACIÓN DEL JUEGO
    ////////////////////////////////////////////////////////////////////////////

    @GetMapping("/game")
    public ModelAndView Game(Principal principal) {
        ModelAndView result = new ModelAndView(VIEWS_GAME_ASIGN_TURN);
        return result;
    }

    @GetMapping("/game/create")
    public String createGame(Principal principal) {
        // Nuevas entidades
        Game game = new Game();
        Round round = new Round();

        // Solicitar a la base de datos
        Player player = playerService.findPlayersByName(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        List<Player> players = lobby.getPlayers();
        List<Card> allCards = cardService.findAllCards();

        // Completar y guardar información en orden
        lobby.setGame(game);
        game.setLobby(lobby);
        round.setGame(game);

        lobbyService.update(lobby);
        gameService.save(game);
        roundService.save(round);

        ////////////////////////////////////////////////////////////////////////
        /// RONDA 0: CARGA INICIAL DE DATOS
        ////////////////////////////////////////////////////////////////////////
        List<Turn> turns = new ArrayList<>();
        Map<Card, Integer> numCardAsigned = new HashMap<>();

        // Preparamos los doblones inciales
        List<Card> doblones = new ArrayList<>();
        Card doblon = allCards.stream()
                .filter(x -> x.getName().equals("doblon"))
                .findFirst().get();
        doblones.add(doblon);
        doblones.add(doblon);
        doblones.add(doblon);

        // Construimos tantos turnos como players
        for (Player p : players) {
            Turn turn = new Turn();
            turn.setRound(round);
            turn.setPlayer(p);
            turn.setDice(0); // por ser ronda 0
            turn.setCards(doblones);

            // Anotamos que se asignan los doblones
            if (numCardAsigned.containsKey(doblon)) {
                Integer v = numCardAsigned.get(doblon);
                v += 3;
            } else {
                numCardAsigned.put(doblon, 3);
            }

            turnService.save(turn);
            turns.add(turn);

        }
        round.setTurns(turns);

        // Creamos las 7 islas
        List<Island> islands = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Island island = new Island();
            island.setGame(game);
            island.setNum(i);
            if (i < 7) { // si no es la isla del mazo
                Integer cartaAleatoria = (int) Math.floor(Math.random() * 10);
                Card card = allCards.get(cartaAleatoria);
                List<Card> cards = new ArrayList<>();
                cards.add(card);
                island.setCards(cards);
                // Anotamos que se asigna la carta
                if (numCardAsigned.containsKey(card)) {
                    Integer v = numCardAsigned.get(card);
                    v++;
                } else {
                    numCardAsigned.put(card, 1);
                }

            } else {
                List<Card> restantes = allCards.stream()
                        .filter(x -> x.getMultiplicity() > (numCardAsigned.get(x) == null ? 0 : numCardAsigned.get(x)))
                        .toList();
                island.setCards(restantes);
            }
            islandService.save(island);
            islands.add(island);
        }
        game.setIslands(islands);

        // Guardamos información
        roundService.update(round);
        gameService.update(game);

        return "redirect:/game";
    }
}
