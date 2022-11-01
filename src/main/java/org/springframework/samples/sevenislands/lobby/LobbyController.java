package org.springframework.samples.sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.user.User;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "views/lobby";

	private final LobbyService lobbyService;

	@Autowired
	public LobbyController(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
	}

	@GetMapping("/lobby")
	public String createLobby(Model model) {
		Lobby lobby = new Lobby();
		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		model.addAttribute("lobby", lobby);
		lobbyService.save(lobby);
		return VIEWS_LOBBY;
	}
}
