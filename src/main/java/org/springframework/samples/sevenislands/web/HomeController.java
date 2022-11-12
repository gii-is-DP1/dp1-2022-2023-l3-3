package org.springframework.samples.sevenislands.web;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.context.spi.CurrentSessionContext;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.sevenislands.lobby.LobbyService;

import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.samples.sevenislands.tools.methods;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final UserService userService;
	private final LobbyService lobbyService;
	private final PlayerService playerService;

	@Autowired
	public HomeController(LobbyService lobbyService,UserService userService, PlayerService playerService) {
		this.userService = userService;
		this.playerService = playerService;
		this.lobbyService = lobbyService;
	}
	
	@GetMapping("/home")
	public String home(HttpServletRequest request, Principal principal) throws ServletException {
		if(userService.checkUserByName(principal.getName())) {
			User user = userService.findUser(principal.getName()).get();
			if(userService.checkUserLobbyByName(user.getNickname())!=null) {
				return "redirect:/lobby/delete";
			} else return "views/home";
		} else {
			request.logout();
			return "redirect:/";
		}
	}
}

