package sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.card.Card;
import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "game/lobby";

	private final LobbyService lobbyService;
	private final GameService gameService;
	private final UserService userService;
	private final LobbyUserService lobbyUserService;

	@Autowired
	public LobbyController(UserService userService, GameService gameService, LobbyService lobbyService, LobbyUserService lobbyUserService) {
		this.lobbyService = lobbyService;
		this.gameService = gameService;
		this.userService = userService;
		this.lobbyUserService = lobbyUserService;
	}

	@GetMapping("/lobby")
	public String joinLobby(HttpServletRequest request, ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) 
	throws NotExistLobbyException, ServletException {
		if(userService.checkUserNoExists(request)) return "redirect:/";
		if(!lobbyUserService.checkUserLobby(logedUser) && !gameService.checkUserGame(logedUser)) return "redirect:/home";
		response.addHeader("Refresh", "1");
		try {
			Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
		
			if (gameService.findGameByUserAndActive(logedUser, true).isPresent()) return "redirect:/game";

			HttpSession session = request.getSession();
			Map<Card,Integer> selectedCards = new TreeMap<Card,Integer>();
			session.setAttribute("selectedCards", selectedCards);

			List<User> players = lobbyUserService.findUsersByLobbyAndMode(lobby, Mode.PLAYER);
			User host = players.get(0);
			model.put("num_players", players.size());
			model.put("lobby", lobby);
			model.put("host", host);
			model.put("logged_player", logedUser);
			model.put("players", players);
			return VIEWS_LOBBY;
		} catch (Exception e) {
			return "redirect:/home";
		}
		
	}

	@GetMapping("/lobby/create")
	public String createLobby(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws ServletException, NotExistLobbyException {
		if(lobbyUserService.checkUserLobby(logedUser)) return "redirect:/home";
		if(gameService.checkUserGame(logedUser)) return "redirect:/home";
		lobbyUserService.createLobby(logedUser);
		return "redirect:/lobby";
	}

	@GetMapping("/join")
	public String join(HttpServletRequest request, ModelMap model, @ModelAttribute("logedUser") User logedUser) throws Exception {
		try {
			if(userService.checkUser(request, logedUser)) return "redirect:/";
		model.put("code", new Lobby());
		return "views/join";
		} catch (Exception e) {
			return "redirect:/home"; 
		}
	}

	@PostMapping("/join")
	public String validateJoin(ModelMap model, @ModelAttribute("code") String code, @ModelAttribute("logedUser") User logedUser) throws NotExistLobbyException {
		List<String> errors = gameService.checkLobbyErrors(code);
		if(errors.isEmpty()) {
			lobbyUserService.joinLobby(code, logedUser);
			return "redirect:/lobby";
		} 
		model.put("errors", errors);
		Lobby lobby2 = new Lobby();
		lobby2.setCode(code);
		model.put("code", lobby2);
		return "views/join";
		
	}
	
	@GetMapping("/lobby/delete/{idEjectedUser}")
	public String ejectPlayer(@ModelAttribute("logedUser") User logedUser, @PathVariable("idEjectedUser") Integer id) throws Exception {
		Optional<User> userEjected = userService.findUserById(id);
		try {
			Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
			List<User> players = lobbyUserService.findUsersByLobby(lobby);
			User host = players.get(0);
			if(!host.equals(logedUser)) return "redirect:/lobby";
			if(userEjected.isPresent() && gameService.ejectPlayer(logedUser, userEjected.get())) return "redirect:/lobby";
			else return "redirect:/home";
		} catch (Exception e) {
			return "redirect:/home";
		}
		
	}
}
