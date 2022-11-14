package sevenislands.tools;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.admin.Admin;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Este componente es una serie de erramientas que pueden ser útiles para todo lo 
 * relacionado con las distintas entidades del proyecto
 */
@Component
public class entityAssistant {

    private static UserService userService;
    private static LobbyService lobbyService;
    private static GameService gameService;
    private static AuthenticationManager authenticationManager;

    @Autowired
	public entityAssistant(AuthenticationManager authenticationManager, GameService gameService, UserService userService, LobbyService lobbyService) {
		this.userService = userService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.authenticationManager = authenticationManager;
	}

    public static Admin parseAdmin(User user) {
        Admin admin = new Admin();
        admin.setId(user.getId());
        admin.setNickname(user.getNickname());
        admin.setPassword(user.getPassword());
        admin.setEnabled(user.isEnabled());
        admin.setFirstName(user.getFirstName());
        admin.setLastName(user.getLastName());
        admin.setEmail(user.getEmail());
        admin.setCreationDate(user.getCreationDate());
        admin.setBirthDate(user.getBirthDate());
        admin.setAvatar("adminAvatar.png");
        admin.setUserType("admin");
        return admin;
    }

    public static Player parsePlayer(User user) {
        Player player = new Player();
        player.setId(user.getId());
        player.setNickname(user.getNickname());
        player.setPassword(user.getPassword());
        player.setEnabled(user.isEnabled());
        player.setFirstName(user.getFirstName());
        player.setLastName(user.getLastName());
        player.setEmail(user.getEmail());
        player.setCreationDate(user.getCreationDate());
        player.setBirthDate(user.getBirthDate());
        player.setAvatar("playerAvatar.png");
        player.setUserType("admin");
        return player;
    }

    public static Game getGameOfPlayer(HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername()).get();
        Lobby lobby = lobbyService.findLobbyByPlayer(user.getId());
        return gameService.findGamebByLobbyId(lobby.getId());
    }

    public static void loginUser(User user, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
