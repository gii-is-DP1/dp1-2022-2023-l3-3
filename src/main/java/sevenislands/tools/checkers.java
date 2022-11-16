package sevenislands.tools;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
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
 * Este componente sirve para hacer todas las comprobaciones relacionadas con el
 * usuario.
 * <p>
 * Principalmente sirve de apoyo para los controladores.
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
    /**
     * Comprueba si un usuario existe en la base de datos o si está baneado.
     * 
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false
     *         (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoExists(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(!userService.checkUserByName(principal.getUsername()) || !userService.findUser(principal.getUsername()).isEnabled()) {
            request.getSession().invalidate();
            request.logout();
            return true;
        } else
            return false;
    }

    /**
     * Comprueba que el usuario existe en la base de datos y que no está baneado.
     * <p>
     * En este caso, si el usuario estaba en una lobby es expulsado.
     * 
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false
     *         (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUser(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(userService.checkUserByName(principal.getUsername()) && userService.findUser(principal.getUsername()).isEnabled()) {
            User user = userService.findUser(principal.getUsername());
            if (lobbyService.checkUserLobbyByName(user.getId())) {
                //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
                Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
                List<User> users = lobby.getPlayerInternal();
                if (users.size() == 1) {
                    lobby.setActive(false);
                }
                users.remove(user);
                lobby.setUsers(users);
                lobbyService.update(lobby);
            }
            return false;
        } else {
            request.getSession().invalidate();
            request.logout();
            return true;
        }
    }

    /**
     * Compruena si el usuario se encuentra en una lobby.
     * 
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (en caso de que no esté en una lobby) o false (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoLobby(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername());

        if (!lobbyService.checkUserLobbyByName(user.getId())) {
            return true;
        }
        return false;
    }

    /**
     * Comprueba si el usuario está en una partida o si existe una ronda asociada a
     * la partida en la que se
     * encuentra el usuario.
     * 
     * @param request (Importar HttpServletRequest request en la función)
     * @return false (en caso de que no esté en una partida, o esta esté empezada) o
     *         true (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoGame(HttpServletRequest request) throws ServletException {
       Optional<Game> game = entityAssistant.getGameOfPlayer(request);
        if (!game.isPresent() || roundService.checkNoRoundByGameId(game.get().getId())) {
            return false;
        }
        return true;
    }

    /**
     * Comprueba si el email pasado como parámetro es válido, es decir que cumpla el patrón "_@_._"
     * @param email
     * @return false (en caso de que el email no sea válido) o true (en caso de que sí lo sea)
     */
    public static Boolean checkEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(regexPattern);
    }

    public static void checkGame(HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername());
        if (lobbyService.checkUserLobbyByName(user.getId())) {
            //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
            Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
            List<User> userList = lobby.getPlayerInternal();
            Game game = gameService.findGamebByLobbyId(lobby.getId()).get();
            List<Round> roundList = roundService.findRoundsByGameId(game.getId()).stream().collect(Collectors.toList());
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
