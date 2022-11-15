package sevenislands.game.turn;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.Game;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.checkers;
import sevenislands.tools.entityAssistant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurnController {

    private static final String VIEWS_GAME = "game/game"; // vista pasiva del juego

    private final TurnService turnService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final LobbyService lobbyService;

    @Autowired
    public TurnController(LobbyService lobbyService, RoundService roundService, TurnService turnService, PlayerService playerService) {
        this.turnService = turnService;
        this.playerService = playerService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
    }

    @GetMapping("/turn")
    public String gameTurn(Principal principal, HttpServletRequest request) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        
        return VIEWS_GAME;
    }

    @GetMapping("/turn/newRound")
    public String gameAsignTurn(Principal principal, HttpServletRequest request) throws ServletException {
        if(checkers.checkUserNoExists(request)) return "redirect:/";
        if(checkers.checkUserNoLobby(request)) return "redirect:/home";
        //TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
        Player player = playerService.findPlayer(principal.getName());
        //TODO: Poner el Game como Optional<Game> y realizar la comprobación de que existe
        Game game = entityAssistant.getGameOfPlayer(request);
        //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
        Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
        List<Player> playerList = lobby.getPlayers();
        List<Round> roundList = roundService.findRoundsByGameId(game.getId()).stream().collect(Collectors.toList());

        Round round = new Round();
        Turn turn = new Turn();

        round.setGame(game);
        turn.setRound(round);

        if(roundService.findRoundsByGameId(game.getId())!=null) {
            turn.setPlayer(player);
        } else if (turnService.findByRoundId(roundList.get(-1).getId()).size() >= playerList.size()) {  //Quizás podríamos poner esta condición
            Integer nextPlayer = (playerList.indexOf(player)+1)%playerList.size();                      //en un método en caso de que lo vayamos
            turn.setPlayer(playerList.get(nextPlayer));                                                 //a tener que comprobar en varios sitios
        } else return "redirect:/turn";

        roundService.save(round);
        turnService.save(turn);
        return "redirect:/turn";
    }

}
