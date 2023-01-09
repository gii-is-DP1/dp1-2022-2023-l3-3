package sevenislands.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.GameService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

	private final UserService userService;
	private final GameService gameService;

	@Autowired
	public HomeController(UserService userService, GameService gameService) {
		this.userService = userService;
		this.gameService = gameService;
	}
	
	@GetMapping("/home")
	public String home(ModelMap model, HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws Exception {
		//turnService.checkUserGame(logedUser);
		if(userService.checkUser(request, logedUser)) return "redirect:/";
		Boolean hasPlayed = gameService.findGameByUser(logedUser).isPresent();
		model.put("hasPlayed", hasPlayed);
		model.put("user", logedUser);
		return "views/home";
		
	}
}