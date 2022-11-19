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

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
import sevenislands.tools.checkers;
import sevenislands.user.User;
import sevenislands.user.UserService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "game/lobby";

	private final LobbyService lobbyService;
	private final GameService gameService;
	private final UserService userService;

	@Autowired
	public LobbyController(UserService userService, GameService gameService, LobbyService lobbyService) {
		this.lobbyService = lobbyService;
		this.gameService = gameService;
		this.userService = userService;
	}

	@GetMapping("/lobby")
	public String joinLobby(HttpServletRequest request, Map<String, Object> model, Principal principal,
			HttpServletResponse response)
			throws NotExistLobbyException, ServletException {
		if (checkers.checkUserNoExists(request))
			return "redirect:/";
		if (checkers.checkUserNoLobby(request))
			return "redirect:/home";

		response.addHeader("Refresh", "1");
		User user = userService.findUser(principal.getName());
		Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
		if (lobbyService.checkUserLobbyByName(user.getId())) {
			if (gameService.findGamebByLobbyId(lobby.getId()).isPresent()) {
				return "redirect:/game";
			}
			User host = lobby.getUsers().get(0);
			model.put("num_players", lobby.getUsers().size());
			model.put("lobby", lobby);
			model.put("host", host);
			model.put("player", user);
			return VIEWS_LOBBY;
		} else {
			return "redirect:/home";
		}
	}

	@GetMapping("/lobby/create")
	public String createLobby(HttpServletRequest request, Principal principal) throws ServletException {
		if(!checkers.checkUserNoLobby(request)) return "redirect:/home";
		User user = userService.findUser(principal.getName());
		Lobby lobby = new Lobby();

		lobby.setCode(lobbyService.generatorCode());
		lobby.setActive(true);
		lobby.addPlayer(user);
		lobbyService.save(lobby);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, Map<String, Object> model) throws ServletException {
		if (checkers.checkUser(request))
			return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
	}

	@PostMapping("/join")
	public String validateJoin(Map<String, Object> model, @ModelAttribute("code") String code, Principal principal)
			throws NotExistLobbyException {
		code = code.trim();
		List<String> errors = new ArrayList<>();
		if (lobbyService.checkLobbyByCode(code)) {
			Lobby lobby = lobbyService.findLobbyByCode(code);
			Integer userNumber = lobby.getUsers().size();
			if (lobby.isActive() == true && userNumber > 0 && userNumber < 4) {
				User user = userService.findUser(principal.getName());
				lobby.addPlayer(user);
				model.put("lobby", lobby);
				lobbyService.update(lobby);
				return "redirect:/lobby";
			}
			if(!lobby.isActive()) errors.add("La partida ya ha empezado");
			if(userNumber == 4) errors.add("La lobby está llena");
		}
		if(!lobbyService.checkLobbyByCode(code)) errors.add("No existe ninguna partida con ese código");
		model.put("errors", errors);
		Lobby lobby = new Lobby();
		lobby.setCode(code);
		model.put("code", lobby);
		return "views/join";
	}

	@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal) {
		User user = userService.findUser(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
		List<User> users = Lobby.getPlayerInternal();
		if (users.size() == 1) {
			Lobby.setActive(false);
		}
		users.remove(user);
		Lobby.setUsers(users);
		lobbyService.update(Lobby);
		return "redirect:/home";
	}

	@GetMapping("/lobby/players")
	public String listaPlayer(Map<String, Object> model, Principal principal, HttpServletResponse response) {
		response.addHeader("Refresh", "2");
		User user = userService.findUser(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
		model.put("players", Lobby.getPlayerInternal());
		return "game/lobbyPlayers";
	}

	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(Principal principal, @PathVariable("id") Integer id) {
		User user = userService.findUser(id);
		Lobby Lobby = lobbyService.findLobbyByPlayer(id).get();
		List<User> users = Lobby.getPlayerInternal();
		if (users.size() == 1) {
			return "redirect:/lobby/delete";
		} else {
			users.remove(user);
			Lobby.setUsers(users);
			lobbyService.update(Lobby);
			if (user.getNickname().equals(principal.getName())) {
				return "redirect:/home";
			} else {
				return "redirect:/lobby/players";
			}

		}
	}

}
