package org.springframework.samples.sevenislands.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
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
	public String home(Principal principal) {
		if(userService.checkUserByName(principal.getName())) {
			return "views/home";
		} else {
			return "redirect:/";
		}
		
	}
}

