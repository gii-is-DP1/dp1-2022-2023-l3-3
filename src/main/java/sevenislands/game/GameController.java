package sevenislands.game;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.card.Card;
import sevenislands.card.CardService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnService;
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
    private final CardService cardService;
    private final RoundService roundService;
    private final TurnService turnService;
    private final IslandService islandService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, LobbyService lobbyService,
            CardService cardService, RoundService roundService, TurnService turnService, IslandService islandService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.cardService = cardService;
        this.roundService = roundService;
        this.turnService = turnService;
        this.islandService = islandService;
    }

    @GetMapping("/game")
    public String createGame(HttpServletRequest request, Principal principal, HttpServletResponse response)
            throws ServletException {
        if (checkers.checkUserNoExists(request))
            return "redirect:/";
        if (checkers.checkUserNoLobby(request))
            return "redirect:/home";
        if (checkers.checkUserNoGame(request))
            return "redirect:/turn";
        response.addHeader("Refresh", "1");

        Player player = playerService.findPlayer(principal.getName());
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        Game game = gameService.findGamebByLobbyId(lobby.getId());
        if (game == null) {
            game = new Game();
            game.setCreationDate(new Date(System.currentTimeMillis()));
            game.setLobby(lobby);
            gameService.save(game);
            lobby.setActive(false);
            lobbyService.update(lobby);
        }

        // Carga inicial de datos
        Round round = new Round();
        round.setGame(game);
        roundService.save(round);
        List<Player> players = lobby.getPlayers();
        List<Card> allCards = cardService.findAllCards();
        List<Turn> turns = new ArrayList<>();
        Map<Card, Integer> numCardAsigned = new HashMap<>();

        // Preparamos doblones y se los damos a los jugadores
        List<Card> doblones = new ArrayList<>();
        Card doblon = allCards.stream()
                .filter(x -> x.getName().equals("doblon"))
                .findFirst().get();
        doblones.add(doblon);
        doblones.add(doblon);
        doblones.add(doblon);
        for (Player p : players) {
            Turn turn = new Turn();
            turn.setRound(round);
            turn.setPlayer(p);
            turn.setDice(null);
            turn.setCards(doblones);
            turnService.save(turn);
            turns.add(turn);

            // Anotamos cartas asignadas
            Integer newValue = 3;
            if (numCardAsigned.containsKey(doblon)) {
                Integer value = numCardAsigned.get(doblon);
                newValue += value;
            }
            numCardAsigned.put(doblon, newValue);
        }
        round.setTurns(turns);
        roundService.update(round);

        // Creamos las 7 islas de la nueva partida
        List<Island> islands = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Island island = new Island();
            island.setGame(game);
            island.setNum(i);

            if (i < 7) { // Si no es la 7ª isla -> asignamos carta
                Integer cartaAleatoria = (int) Math.floor(Math.random() * 10);
                List<Card> cards = new ArrayList<>();
                Card card = allCards.get(cartaAleatoria);
                cards.add(card);
                island.setCards(cards);

                // Anotamos cartas asignadas
                Integer newValue = 3;
                if (numCardAsigned.containsKey(card)) {
                    Integer value = numCardAsigned.get(card);
                    newValue += value;
                }
                numCardAsigned.put(card, newValue);
            } else { // Si es la 7ª -> asignamos cartas restantes
                List<Card> restantes = allCards.stream()
                        .filter(x -> x.getMultiplicity() > (numCardAsigned.get(x) == null ? 0
                                : numCardAsigned.get(x)))
                        .collect(Collectors.toList());
                island.setCards(restantes);
            }
            islandService.save(island);
            islands.add(island);
        }

        // Actualizamos y guardamos información
        game.setIslands(islands);
        gameService.update(game);

        return VIEWS_GAME_ASIGN_TURN;
    }
}
