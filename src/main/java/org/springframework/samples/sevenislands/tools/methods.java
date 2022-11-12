package org.springframework.samples.sevenislands.tools;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.lobby.LobbyService;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class methods {

    private static UserService userService;
    public static PlayerService playerService;
    public static LobbyService lobbyService;

    @Autowired
	public methods(UserService userService, PlayerService playerService, LobbyService lobbyService) {
		this.userService = userService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
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
            if (userService.checkUserLobbyByName(user.getNickname())) {
                Player player = playerService.findPlayer(principal.getUsername());
                Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
                List<Player> players = lobby.getPlayerInternal();
                if (players.size() == 1) {
                    lobby.setActive(false);
                }
                player.setLobby(null);
                playerService.update(player);
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

        if (!userService.checkUserLobbyByName(user.getNickname())) {
            return true;
        } return false;
    }


}
