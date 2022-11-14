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

@Component
public class methods {

    private static UserService userService;
    private static PlayerService playerService;
    private static LobbyService lobbyService;
    private static RoundService roundService;
    private static GameService gameService;
    private static AuthenticationManager authenticationManager;

    @Autowired
	public methods(AuthenticationManager authenticationManager, GameService gameService, RoundService roundService, UserService userService, PlayerService playerService, LobbyService lobbyService) {
		this.userService = userService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.roundService = roundService;
        this.gameService = gameService;
        this.authenticationManager = authenticationManager;
	}

    public static Boolean checkUserNoExists(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(!userService.checkUserByName(principal.getUsername()) || !userService.findUser(principal.getUsername()).get().isEnabled()) {
            request.getSession().invalidate();
            request.logout();
            return true;
        } else return false;
    }

    public static Boolean checkUser(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(userService.checkUserByName(principal.getUsername()) && userService.findUser(principal.getUsername()).get().isEnabled()) {
            User user = userService.findUser(principal.getUsername()).get();
            if (lobbyService.checkUserLobbyByName(user.getId())) {
                Player player = playerService.findPlayer(principal.getUsername());
                Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
                List<Player> players = lobby.getPlayerInternal();
                if (players.size() == 1) {
                    lobby.setActive(false);
                }
                players.remove(player);
                lobby.setPlayers(players);
                lobbyService.update(lobby);
            }
        } else {
            request.getSession().invalidate();
            request.logout();
            return true;
        }
        return false;
    }

    public static Boolean checkUserNoLobby(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(principal.getUsername()).get();

        if (!lobbyService.checkUserLobbyByName(user.getId())) {
            return true;
        } return false;
    }

    public static Boolean checkUserNoGame(HttpServletRequest request) throws ServletException {
        Game game = getGameOfPlayer(request);
        if (game==null || roundService.checkGameByLobbyId(game.getId())) {
            return false;
        } 
        return true;
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
