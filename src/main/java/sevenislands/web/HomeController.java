package sevenislands.web;

import java.security.Principal;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.tools.checkers;
import sevenislands.user.User;
import sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final UserService userService;

	@Autowired
	public HomeController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/home")
	public String home(Map<String, Object> model, HttpServletRequest request, Principal principal) throws ServletException {
		checkers.checkGame(request);
		if(checkers.checkUser(request)) return "redirect:/";
		User user = userService.findUser(principal.getName()).get();      
		model.put("user", user);
		return "views/home";
	}
}

