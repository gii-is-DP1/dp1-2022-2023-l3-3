package sevenislands.web;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.user.User;
import sevenislands.user.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class WelcomeController {

	private final UserService userService;

	@Autowired
	public WelcomeController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String welcome(ModelMap model, @ModelAttribute("logedUser") User logedUser) {	 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !userService.checkUserByNickname(logedUser.getNickname())){
			return "welcome";
		} else {
			return "redirect:/home";
		}
	}
}
