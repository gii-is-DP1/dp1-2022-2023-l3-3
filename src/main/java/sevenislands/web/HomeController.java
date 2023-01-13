package sevenislands.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.invitation.Invitation;
import sevenislands.user.invitation.InvitationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	private final GameService gameService;
	private final InvitationService invitationService;
	private final LobbyUserService lobbyUserService;

	@Autowired
	public HomeController(GameService gameService, InvitationService invitationService, LobbyUserService lobbyUserService) {
		this.gameService = gameService;
		this.invitationService = invitationService;
		this.lobbyUserService = lobbyUserService;
	}
	
	@GetMapping("/home")
	public String home(ModelMap model, HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws Exception {
		//turnService.checkUserGame(logedUser);
		if(invitationService.checkUser(request, logedUser)) return "redirect:/";
		List<Invitation> invitations = invitationService.findInvitationsByReceiver(logedUser);
		Boolean hasPlayed = gameService.findGameByUserAndMode(logedUser, Mode.PLAYER).isPresent();
		model.put("code", new Lobby());
		model.put("invitations", invitations);
		model.put("hasPlayed", hasPlayed);
		model.put("user", logedUser);
		return "views/home";	
	}

	@PostMapping("/home")
	public String validateJoin(ModelMap model, @ModelAttribute("code") String code, @ModelAttribute("logedUser") User logedUser) throws NotExistLobbyException {
		List<String> errors = gameService.checkLobbyErrors(code);
		if(errors.isEmpty()) {
			lobbyUserService.joinLobby(code, logedUser, Mode.PLAYER);
			invitationService.deleteInvitationsByLobbyAndUser(lobbyUserService.findLobbyByUser(logedUser), logedUser);
			return "redirect:/lobby";
		}
		List<Invitation> invitations = invitationService.findInvitationsByReceiver(logedUser);
		Boolean hasPlayed = gameService.findGameByUserAndMode(logedUser, Mode.PLAYER).isPresent();
		Lobby lobby2 = new Lobby();
		lobby2.setCode(code);
		model.put("errors", errors);
		model.put("code", lobby2);
		model.put("invitations", invitations);
		model.put("hasPlayed", hasPlayed);
		model.put("user", logedUser);
		return "views/home";
	}
}