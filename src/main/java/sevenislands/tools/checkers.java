package sevenislands.tools;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Este componente sirve para hacer todas las comprobaciones relacionadas con el usuario.
 * <p> Principalmente sirve de apoyo para los controladores.
 */
@Component
public class checkers {

    private static UserService userService;
    private static LobbyService lobbyService;
    private static RoundService roundService;
    private static GameService gameService;
    private static TurnService turnService;

    @Autowired
	public checkers(TurnService turnService, GameService gameService, RoundService roundService, UserService userService, LobbyService lobbyService) {
		this.userService = userService;
        this.lobbyService = lobbyService;
        this.roundService = roundService;
        this.gameService = gameService;
        this.turnService = turnService;
	}

    public static void checkGame() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByNickname(principal.getUsername());
        if (gameService.findGameByNickname(principal.getUsername()).isPresent()) {

            //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
            Lobby lobby = lobbyService.findLobbyByPlayerId(user.getId()).get();
            Optional<Game> game = gameService.findGamebByLobbyId(lobby.getId());
            List<User> userList = lobby.getPlayerInternal();
            List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
            if(roundList.size()!=0) {
                Round lastRound = roundList.get(roundList.size()-1);
                List<Turn> turnList = turnService.findByRoundId(lastRound.getId());
                Turn lastTurn = turnList.get(turnList.size()-1);
                if(lastTurn.getUser().getId()==user.getId()) {
                    Turn turn = new Turn();
                    Integer nextUser = (userList.indexOf(user)+1)%userList.size();
                    turn.setStartTime(LocalDateTime.now());
                    turn.setRound(lastRound);
                    turn.setUser(userList.get(nextUser));
                    turnService.save(turn);
                }
            }
        }
    }
}
