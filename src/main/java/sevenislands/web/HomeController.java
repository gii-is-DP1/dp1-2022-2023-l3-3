package sevenislands.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.turn.TurnService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

	private final UserService userService;
	private final TurnService turnService;

	@Autowired
	public HomeController(UserService userService, TurnService turnService) {
		this.userService = userService;
		this.turnService = turnService;
	}
	
	@GetMapping("/home")
	public String home(ModelMap model, HttpServletRequest request, @ModelAttribute("logedUser") User logedUser) throws Exception {
		//turnService.checkUserGame(logedUser);
		if(userService.checkUser2(request, logedUser)) return "redirect:/";   
		model.put("user", logedUser);
		return "views/home";
		
	}
}