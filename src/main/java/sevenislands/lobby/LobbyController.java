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
	public String joinLobby(HttpServletRequest request, Map<String, Object> model, Principal principal, HttpServletResponse response) 
	throws NotExistLobbyException, ServletException {
		if(userService.checkUserNoExists(request)) return "redirect:/";
		if(checkers.checkUserNoLobby(request)) return "redirect:/home";
		
		response.addHeader("Refresh", "1");
		User user = userService.findUserByNickname(principal.getName());
		Lobby lobby = lobbyService.findLobbyByPlayerId(user.getId()).get();
		if (lobbyService.findLobbyByPlayerId(user.getId())!=null) {
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
		User user = userService.findUserByNickname(principal.getName());
		lobbyService.createLobby(user);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, Map<String, Object> model) throws ServletException {
		if(userService.checkUser(request)) return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
	}

	@PostMapping("/join")
	public String validateJoin(Map<String, Object> model, @ModelAttribute("code") String code, Principal principal) throws NotExistLobbyException {
		User user = userService.findUserByNickname(principal.getName());
		if(lobbyService.validateJoin(code, user)) return "redirect:/lobby";
		List<String> errors = lobbyService.checkLobbyErrors(code);
		
		model.put("errors", errors);
		Lobby lobby2 = new Lobby();
		lobby2.setCode(code);
		model.put("code", lobby2);
		return "views/join";
		
	}

	@GetMapping("/lobby/delete")
	public String leaveLobby(Principal principal) {
		User user = userService.findUserByNickname(principal.getName());
		lobbyService.leaveLobby(user);
		return "redirect:/home";
	}

	@GetMapping("/lobby/players")
	public String listaPlayer(Map<String, Object> model, Principal principal, HttpServletResponse response) {
		response.addHeader("Refresh", "2");
		User user = userService.findUserByNickname(principal.getName());
		Lobby Lobby = lobbyService.findLobbyByPlayerId(user.getId()).get();
		model.put("players", Lobby.getPlayerInternal());
		return "game/lobbyPlayers";
	}
	//TODO:
	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(Principal principal, @PathVariable("id") Integer id) {
		User authUser = userService.findUserByNickname(principal.getName());
		User userEjected = userService.findUser(id);
		if(lobbyService.ejectPlayer(authUser, userEjected)) return "redirect:/lobby/players";
		return "redirect:/home";
	}
}
