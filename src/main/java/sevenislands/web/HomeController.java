package sevenislands.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.GameService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.invitation.Invitation;
import sevenislands.user.invitation.InvitationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

	private final UserService userService;
	private final GameService gameService;
	private final InvitationService invitationService;

	@Autowired
	public HomeController(UserService userService, GameService gameService, InvitationService invitationService) {
		this.userService = userService;
		this.gameService = gameService;
		this.invitationService = invitationService;
	}
	
	@GetMapping("/home")
	public String home(ModelMap model, HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws Exception {
		//turnService.checkUserGame(logedUser);
		if(userService.checkUser(request, logedUser)) return "redirect:/";
		List<Invitation> invitations = invitationService.findInvitationsByReceiver(logedUser);
		Boolean hasPlayed = gameService.findGameByUser(logedUser).isPresent();
		model.put("invitations", invitations);
		model.put("hasPlayed", hasPlayed);
		model.put("user", logedUser);
		return "views/home";
		
	}
}