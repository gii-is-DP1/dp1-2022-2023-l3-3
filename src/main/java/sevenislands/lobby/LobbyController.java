package sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
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
	public String joinLobby(HttpServletRequest request, ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) 
	throws NotExistLobbyException, ServletException {
		if(userService.checkUserNoExists(request)) return "redirect:/";
		if(lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
		
		response.addHeader("Refresh", "1");
		Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
		if (lobbyService.findLobbyByPlayerId(logedUser.getId())!=null) {
			if (gameService.findGamebByLobbyId(lobby.getId()).isPresent()) {
				return "redirect:/game";
			}
			User host = lobby.getUsers().get(0);
			model.put("num_players", lobby.getUsers().size());
			model.put("lobby", lobby);
			model.put("host", host);
			model.put("player", logedUser);
			return VIEWS_LOBBY;
		} else {
			return "redirect:/home";
		}
	}

	@GetMapping("/lobby/create")
	public String createLobby(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws ServletException {
		if(!lobbyService.checkUserNoLobby(logedUser)) return "redirect:/home";
		lobbyService.createLobby(logedUser);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, ModelMap model) throws ServletException {
		if(userService.checkUser(request)) return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
	}

	@PostMapping("/join")
	public String validateJoin(ModelMap model, @ModelAttribute("code") String code, @ModelAttribute("logedUser") User logedUser) throws NotExistLobbyException {
		if(lobbyService.validateJoin(code, logedUser)) return "redirect:/lobby";
		List<String> errors = lobbyService.checkLobbyErrors(code);
		
		model.put("errors", errors);
		Lobby lobby2 = new Lobby();
		lobby2.setCode(code);
		model.put("code", lobby2);
		return "views/join";
		
	}

	@GetMapping("/lobby/delete")
	public String leaveLobby(@ModelAttribute("logedUser") User logedUser) {
		lobbyService.leaveLobby(logedUser);
		return "redirect:/home";
	}

	@GetMapping("/lobby/players")
	public String listaPlayer(ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) {
		response.addHeader("Refresh", "2");
		Lobby Lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
		model.put("players", Lobby.getPlayerInternal());
		return "game/lobbyPlayers";
	}
	//TODO:
	@GetMapping("/lobby/players/delete/{id}")
	public String ejectPlayer(@ModelAttribute("logedUser") User logedUser, @PathVariable("id") Integer id) {
		User userEjected = userService.findUser(id);
		if(lobbyService.ejectPlayer(logedUser, userEjected)) return "redirect:/lobby/players";
		return "redirect:/home";
	}
}
