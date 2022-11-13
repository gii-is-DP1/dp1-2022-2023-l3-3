package sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.GameService;
import sevenislands.lobby.exceptions.NotExistLobbyException;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.methods;
import sevenislands.user.UserService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "game/lobby";

	private final LobbyService lobbyService;
	private final UserService userService;
	private final PlayerService playerService;
	private final GameService gameService;

	@Autowired
	public LobbyController(GameService gameService, LobbyService lobbyService, PlayerService playerService, UserService userService) {
		this.lobbyService = lobbyService;
		this.playerService = playerService;
		this.userService = userService;
		this.gameService = gameService;
	}

	@GetMapping("/lobby")
	public String joinLobby(HttpServletRequest request, Map<String, Object> model, Principal principal, HttpServletResponse response) 
	throws NotExistLobbyException, ServletException {
		if(methods.checkUserNoExists(request)) return "redirect:/";
		if(methods.checkUserNoLobby(request)) return "redirect:/home";
		
		response.addHeader("Refresh", "1");

		Player player = playerService.findPlayer(principal.getName());
		Lobby lobby = lobbyService.findLobbyByPlayer(player.getId());

		if (lobbyService.checkUserLobbyByName(player.getId())) {
			if (gameService.findGamebByLobbyId(lobby.getId())!=null) return "redirect:/game";
			Player host = lobby.getPlayers().get(0);
			model.put("lobby", lobby);
			model.put("host", host);
			model.put("player", player);
			return VIEWS_LOBBY;
		} else {
			return "redirect:/home";
		}
	}

	@GetMapping("/lobby/create")
	public String createLobby(HttpServletRequest request, Principal principal) throws ServletException {
		if(!methods.checkUserNoLobby(request)) return "redirect:/home";
		Player player = playerService.findPlayer(principal.getName());
		Lobby lobby = new Lobby();

		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		lobby.addPlayer(player);
		lobbyService.save(lobby);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, Map<String, Object> model) throws ServletException {
		if(methods.checkUser(request)) return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
	}

	@PostMapping("/join")
	public String validateJoin(Map<String, Object> model, @ModelAttribute("code") String code, Principal principal) throws NotExistLobbyException {
		code = code.trim();
		if (lobbyService.checkLobbyByCode(code)) {
			Lobby lobby = lobbyService.findLobbyByCode(code);
			Integer players = lobby.getPlayers().size();
			if (lobby.isActive() == true && players > 0 && players < 4) {
				Player player = playerService.findPlayer(principal.getName());
				lobby.addPlayer(player);
				model.put("lobby", lobby);
				lobbyService.update(lobby);
				return "redirect:/lobby";
			} else
				return "redirect:/join";
		} else
			return "redirect:/join";
	}

	// cambiar relacion onetoOne entre jugador y lobby
	@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal) {
		Player player = playerService.findPlayer(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId());
		List<Player> players = Lobby.getPlayerInternal();
		if (players.size() == 1) {
			Lobby.setActive(false);
		}
		players.remove(player);
		Lobby.setPlayers(players);
		lobbyService.update(Lobby);
		return "redirect:/home";
	}

	@GetMapping("/lobby/players")
	public String listaPlayer(Map<String, Object> model, Principal principal, HttpServletResponse response) {
		response.addHeader("Refresh", "2");
		Player player = playerService.findPlayer(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId());
		model.put("players", Lobby.getPlayerInternal());
		return "game/lobbyPlayers";
	}

	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(Principal principal, @PathVariable("id") Integer id) {
		Player player = playerService.findPlayer(id);
		Lobby Lobby = lobbyService.findLobbyByPlayer(id);
		List<Player> players = Lobby.getPlayerInternal();
		if (players.size() == 1) {
			return "redirect:/lobby/delete";
		} else {
			players.remove(player);
			Lobby.setPlayers(players);
			lobbyService.update(Lobby);
			if (player.getNickname().equals(principal.getName())) {
				return "redirect:/home";
			} else {
				return "redirect:/lobby/players";
			}

		}
	}

}