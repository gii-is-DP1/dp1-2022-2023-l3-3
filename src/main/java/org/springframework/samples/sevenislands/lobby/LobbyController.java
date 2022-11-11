package org.springframework.samples.sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.exceptions.NotExistLobbyException;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.samples.sevenislands.user.UserService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "views/lobby";

	private final LobbyService lobbyService;
	private final UserService userService;
	private final PlayerService playerService;

	@Autowired
	public LobbyController(LobbyService lobbyService, PlayerService playerService, UserService userService) {
		this.lobbyService = lobbyService;
		this.playerService = playerService;
		this.userService = userService;
	}

	@GetMapping("/lobby")
	public ModelAndView joinLobby(Principal principal, HttpServletResponse response) throws NotExistLobbyException {
		response.addHeader("Refresh", "2");
		ModelAndView result = new ModelAndView(VIEWS_LOBBY);
		ModelAndView result2 = new ModelAndView("redirect:/home");
		ModelAndView result3 = new ModelAndView("redirect:/game");
		Player player = playerService.findPlayersByName(principal.getName());
		Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());
		if (player.isEnabled() == false || !userService.checkUserByName(principal.getName())) {
			return new ModelAndView("redirect:/");
		}

		if (userService.checkUserLobbyByName(player.getNickname()) != null) {
			if (lobby.getGame() != null) {
				return result3;
			}
			Player host = lobby.getPlayers().get(0);
			result.addObject("lobby", lobby);
			result.addObject("host", host);
			result.addObject("player", player);
			return result;
		} else {
			return result2;
		}
	}

	@GetMapping("/lobby/create")
	public String createLobby(Principal principal) {
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
	public ModelAndView validateJoin(@ModelAttribute("code") String code, Principal principal)
			throws NotExistLobbyException {
		ModelAndView result = new ModelAndView("redirect:/lobby");
		ModelAndView result2 = new ModelAndView("redirect:/join");

		if (lobbyService.checkLobbyByCode(code)) {
			Lobby lobby = lobbyService.findLobbyByCode(code);
			Integer players = lobby.getPlayers().size();

			if (lobby.isActive() == true && players > 0 && players < 4) {
				Player player = playerService.findPlayersByName(principal.getName());
				lobby.addPlayer(player);
				result.addObject("lobby", lobby);
				lobbyService.update(lobby);
				return result;
			} else
				return result2;
		} else
			return result2;
	}

	// cambiar relacion onetoOne entre jugador y lobby
	@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal) {
		Player player = playerService.findPlayersByName(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId());
		List<Player> players = Lobby.getPlayerInternal();
		if (players.size() == 1) {
			Lobby.setActive(false);
		}
		player.setLobby(null);
		playerService.update(player);
		players.remove(player);
		Lobby.setPlayers(players);
		lobbyService.update(Lobby);
		return "redirect:/home";
	}

	@GetMapping("/lobby/players")
	public ModelAndView listaPlayer(Principal principal, HttpServletResponse response) {
		response.addHeader("Refresh", "2");
		ModelAndView result = new ModelAndView("views/lobbyPlayers");
		Player player = playerService.findPlayersByName(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId());
		result.addObject("players", Lobby.getPlayerInternal());
		return result;
	}

	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(Principal principal, @PathVariable("id") Integer id) {
		Player player = playerService.findPlayersById(id);
		Lobby Lobby = lobbyService.findLobbyByPlayer(id);
		List<Player> players = Lobby.getPlayerInternal();
		if (players.size() == 1) {
			return "redirect:/lobby/delete";
		} else {
			players.remove(player);
			Lobby.setPlayers(players);
			lobbyService.update(Lobby);
			player.setLobby(null);
			playerService.update(player);
			if (player.getNickname().equals(principal.getName())) {
				return "redirect:/home";
			} else {
				return "redirect:/lobby/players";
			}

		}
	}

}
