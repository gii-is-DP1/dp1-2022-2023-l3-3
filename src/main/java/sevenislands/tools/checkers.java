package sevenislands.tools;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.game.Game;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
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
    private static PlayerService playerService;
    private static LobbyService lobbyService;
    private static RoundService roundService;

    @Autowired
	public checkers(RoundService roundService, UserService userService, PlayerService playerService, LobbyService lobbyService) {
		this.userService = userService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.roundService = roundService;
	}
    /**
     * Comprueba si un usuario existe en la base de datos o si está baneado.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoExists(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(!userService.checkUserByName(principal.getUsername()) || !userService.findUser(principal.getUsername()).get().isEnabled()) {
            request.getSession().invalidate();
            request.logout();
            return true;
        } else return false;
    }

    /**
     * Comprueba que el usuario existe en la base de datos y que no está baneado.
     * <p> En este caso, si el usuario estaba en una lobby es expulsado.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUser(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(userService.checkUserByName(principal.getUsername()) && userService.findUser(principal.getUsername()).get().isEnabled()) {
            User user = userService.findUser(principal.getUsername()).get();
            if (lobbyService.checkUserLobbyByName(user.getId())) {
                //TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
                Player player = playerService.findPlayer(principal.getUsername());
                //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
                Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
                List<Player> players = lobby.getPlayerInternal();
                if (players.size() == 1) {
                    lobby.setActive(false);
                }
                players.remove(player);
                lobby.setPlayers(players);
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
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (en caso de que no esté en una lobby) o false (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoLobby(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername()).get();

        if (!lobbyService.checkUserLobbyByName(user.getId())) {
            return true;
        } return false;
    }

    /**
     * Comprueba si el usuario está en una partida o si existe una ronda asociada a la partida en la que se
     * encuentra el usuario.
     * @param request (Importar HttpServletRequest request en la función)
     * @return false (en caso de que no esté en una partida, o esta esté empezada) o true (en otro caso)
     * @throws ServletException
     */
    public static Boolean checkUserNoGame(HttpServletRequest request) throws ServletException {
       Optional<Game> game = entityAssistant.getGameOfPlayer(request);
        if (!game.isPresent() || roundService.checkGameByGameId(game.get().getId())) {
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
}
