package sevenislands.web;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.user.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	private final UserService userService;

	@Autowired
	public WelcomeController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String welcome(Map<String, Object> model, Principal principal) {	 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !userService.checkUserByName(principal.getName())){
			return "welcome";
		} else {
			return "redirect:/home";
		}
	}
}
