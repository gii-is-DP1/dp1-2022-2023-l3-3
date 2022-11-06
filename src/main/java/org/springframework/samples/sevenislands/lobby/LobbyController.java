package org.springframework.samples.sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "views/lobby";

	private final LobbyService lobbyService;

	@Autowired
	public LobbyController(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
	}
/* 
	@GetMapping("/lobby")
	public String createLobby(Model model) {
		Lobby lobby = new Lobby();
		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		model.addAttribute("lobby", lobby);
		lobbyService.save(lobby);
		return VIEWS_LOBBY;
	}*/
	@GetMapping("/lobby")
	public ModelAndView createLobby(){
		ModelAndView result=new ModelAndView(VIEWS_LOBBY);
		Lobby lobby = new Lobby();
		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		result.addObject("lobby", lobby);
		lobbyService.save(lobby);
		return result;
	}

}
