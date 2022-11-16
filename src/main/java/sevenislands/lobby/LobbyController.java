package sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
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
import sevenislands.tools.checkers;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "game/lobby";

	private final LobbyService lobbyService;
	private final PlayerService playerService;
	private final GameService gameService;

	@Autowired
	public LobbyController(GameService gameService, LobbyService lobbyService, PlayerService playerService) {
		this.lobbyService = lobbyService;
		this.playerService = playerService;
		this.gameService = gameService;
	}

	@GetMapping("/lobby")
	public String joinLobby(HttpServletRequest request, Map<String, Object> model, Principal principal, HttpServletResponse response) 
	throws NotExistLobbyException, ServletException {
		if(checkers.checkUserNoExists(request)) return "redirect:/";
		if(checkers.checkUserNoLobby(request)) return "redirect:/home";
		
		response.addHeader("Refresh", "1");
		//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
		Player player = playerService.findPlayer(principal.getName()).get();
		//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
		Lobby lobby = lobbyService.findLobbyByPlayer(player.getId()).get();
		if (lobbyService.checkUserLobbyByName(player.getId())) {
			if (gameService.findGamebByLobbyId(lobby.getId()).isPresent()) {
				return "redirect:/game";
			}
			Player host = lobby.getPlayers().get(0);
			model.put("num_players", lobby.getPlayers().size());
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
		if(!checkers.checkUserNoLobby(request)) return "redirect:/home";
		//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
		Player player = playerService.findPlayer(principal.getName()).get();
		Lobby lobby = new Lobby();

		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		lobby.addPlayer(player);
		lobbyService.save(lobby);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, Map<String, Object> model) throws ServletException {
		if(checkers.checkUser(request)) return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
	}

	@PostMapping("/join")
	public String validateJoin(Map<String, Object> model, @ModelAttribute("code") String code, Principal principal) throws NotExistLobbyException {
		code = code.trim();
		List<String> errors = new ArrayList<>();
		if (lobbyService.checkLobbyByCode(code)) {
			//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
			Lobby lobby = lobbyService.findLobbyByCode(code);
			Integer players = lobby.getPlayers().size();
			if (lobby.isActive() == true && players > 0 && players < 4) {
				//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
				Player player = playerService.findPlayer(principal.getName()).get();
				lobby.addPlayer(player);
				model.put("lobby", lobby);
				lobbyService.update(lobby);
				return "redirect:/lobby";
			}
			if(!lobby.isActive()) errors.add("La partida ya ha empezado");
			if(players == 4) errors.add("La lobby está llena");
		}
		if(!lobbyService.checkLobbyByCode(code)) errors.add("No existe ninguna partida con ese código");
		model.put("errors", errors);
		Lobby lobby = new Lobby();
		lobby.setCode(code);
		model.put("code", lobby);
		return "views/join";
	}

	// cambiar relacion onetoOne entre jugador y lobby
	@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal) {
		//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
		Player player = playerService.findPlayer(principal.getName()).get();
		//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId()).get();
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
		//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
		Player player = playerService.findPlayer(principal.getName()).get();
		//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
		Lobby Lobby = lobbyService.findLobbyByPlayer(player.getId()).get();
		model.put("players", Lobby.getPlayerInternal());
		return "game/lobbyPlayers";
	}

	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(Principal principal, @PathVariable("id") Integer id) {
		//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
		Player player = playerService.findPlayer(id).get();
		//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
		Lobby Lobby = lobbyService.findLobbyByPlayer(id).get();
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
