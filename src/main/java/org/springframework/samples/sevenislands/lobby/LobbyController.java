package org.springframework.samples.sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.lobbyExceptions.NotExistLobbyException;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "views/lobby";

	private final LobbyService lobbyService;

	private final PlayerService playerService;

	@Autowired
	public LobbyController(LobbyService lobbyService, PlayerService playerService) {
		this.lobbyService = lobbyService;
		this.playerService = playerService;
	}

	@GetMapping("/lobby")
	public ModelAndView joinLobby(Principal principal){
		ModelAndView result = new ModelAndView(VIEWS_LOBBY);
		Player player = playerService.findPlayersByName(principal.getName());
		Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
		Player host = lobby.getPlayers().get(0);
		result.addObject("lobby", lobby);
		result.addObject("host", host);
		result.addObject("player", player);
		return result;
	}

	@GetMapping("/lobby/create")
	public String createLobby(Principal principal){
		Player player = playerService.findPlayersByName(principal.getName());
		Lobby lobby = new Lobby(); 
		
		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		lobby.addPlayer(player);
		lobbyService.save(lobby);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public ModelAndView join() {
		ModelAndView result = new ModelAndView("views/join");
		result.addObject("code", new Lobby());
		return result;
	}

	@PostMapping("/join")
	public ModelAndView validateJoin(@ModelAttribute("code") String code, Principal principal) throws NotExistLobbyException {
		Lobby lobby = lobbyService.findLobbyByCode(code);
		
		Integer players = lobby.getPlayers().size();
		ModelAndView result = new ModelAndView("redirect:/lobby");
		ModelAndView result2 = new ModelAndView("redirect:/oups");
		if(lobby.isActive()==true && players>0 && players<4){
			Player player = playerService.findPlayersByName(principal.getName());
			lobby.addPlayer(player);
			result.addObject("lobby", lobby);
			lobbyService.update(lobby);
			return result;
		}
		return result2;
	}

//cambiar relacion onetoOne entre jugador y lobby
	@GetMapping("/lobby/delete")
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
	}


	@GetMapping("/lobby/players")
	public ModelAndView listaPlayer(Principal principal){
		ModelAndView result = new ModelAndView("views/lobbyPlayers");
		Player player=playerService.findPlayersByName(principal.getName());
		Lobby LobbyID=lobbyService.findLobbyByPlayer(player.getId());
		result.addObject("players", LobbyID.getPlayerInternal());
		return result;
	}

}
