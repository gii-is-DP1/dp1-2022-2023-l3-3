package sevenislands.lobby;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
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
import sevenislands.enums.Status;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.friend.FriendService;
import sevenislands.user.invitation.Invitation;
import sevenislands.user.invitation.InvitationService;

@Controller
public class LobbyController {

	private static final String VIEWS_LOBBY = "game/lobby";

	private final GameService gameService;
	private final UserService userService;
	private final LobbyUserService lobbyUserService;
	private final FriendService friendService;
	private final InvitationService invitationService;

	@Autowired
	public LobbyController(UserService userService, GameService gameService, 
	LobbyUserService lobbyUserService, FriendService friendService,
	InvitationService invitationService) {
		this.gameService = gameService;
		this.userService = userService;
		this.lobbyUserService = lobbyUserService;
		this.friendService = friendService;
		this.invitationService = invitationService;
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
			List<User> friends = friendService.findUserFriends(logedUser, Status.ACCEPTED);
			friends.removeAll(players);
			
			model.put("friends", friends);
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
		invitationService.deleteInvitationsByLobbyAndUser(lobbyUserService.findLobbyByUser(logedUser), logedUser);
		lobbyUserService.createLobby(logedUser);
		return "redirect:/lobby";
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

	@GetMapping("/lobby/accept/{idInvitationReceiver}")
	public String acceptInvitation(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser, @PathVariable("idInvitationReceiver") Integer id) throws Exception {
		Optional<Invitation> invitation = invitationService.findInvitationById(id);
		if(!invitation.isPresent()) return "redirect:/home";
		Lobby lobby = invitation.get().getLobby();
		Mode mode = invitation.get().getMode();
		invitationService.deleteInvitationsByLobbyAndUser(invitation.get().getLobby(), logedUser);
		if(gameService.findGameByLobby(lobby).isPresent()) mode = Mode.VIEWER;
		lobbyUserService.joinLobby(lobby.getCode(), logedUser, mode);
		
		HttpSession session = request.getSession();
		session.setAttribute("selectedCards", new HashMap<>());
		return "redirect:/lobby";
	}
}
