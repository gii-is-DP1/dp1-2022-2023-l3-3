package org.springframework.samples.sevenislands.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    
    private static final String VIEWS_CONTROL_PANEL = "views/controlPanel";

    private final UserService userService;

    @Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

    @GetMapping("/controlPanel")
	public ModelAndView listUsers(Principal principal, HttpServletResponse response){
		response.addHeader("Refresh", "5");
        Admin a = new Admin();
		ModelAndView result = new ModelAndView(VIEWS_CONTROL_PANEL);
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());      
        result.addObject("users", users);
		return result;
	}

    /*@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal){
		Player player=playerService.findPlayersByName(principal.getName());
		Lobby LobbyID=lobbyService.findLobbyByPlayer(player.getId());
		List<Player> players=LobbyID.getPlayerInternal();
		if(players.size()==1){
			boolean active=false;
			players.remove(player);
			LobbyID.setPlayers(players);
			LobbyID.setActive(active);
			lobbyService.update(LobbyID);	
		}else{
			players.remove(player);
			LobbyID.setPlayers(players);
			lobbyService.update(LobbyID);
		
		}
		return "redirect:/home";
	}*/

}
