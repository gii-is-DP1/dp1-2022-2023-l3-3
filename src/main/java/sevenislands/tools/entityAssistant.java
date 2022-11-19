package sevenislands.tools;


import java.util.Optional;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Este componente es una serie de erramientas que pueden ser útiles para todo
 * lo
 * relacionado con las distintas entidades del proyecto
 */
@Component
public class entityAssistant {

    private static UserService userService;
    private static LobbyService lobbyService;
    private static GameService gameService;
    private static AuthenticationManager authenticationManager;

    @Autowired
    public entityAssistant(AuthenticationManager authenticationManager, GameService gameService,
            UserService userService, LobbyService lobbyService) {
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Obtiene la partida del jugador actual en caso de que esté en una. 
     * @param request
     * @return Game
     */
    public static Optional<Game> getGameOfPlayer(HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername());
        //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
        Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
        //TODO: Poner el Game como Optional<Game> y realizar la comprobación de que existe
        return gameService.findGamebByLobbyId(lobby.getId());
    }

    /**
     * Realiza el logeo automático del usuario actual.
     * @param user
     * @param password
     */
    public static void loginUser(User user, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(),
                password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
