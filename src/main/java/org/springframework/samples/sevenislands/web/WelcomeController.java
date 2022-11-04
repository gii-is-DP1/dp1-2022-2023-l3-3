package org.springframework.samples.sevenislands.web;

import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
			return "welcome";
		} else {
			return "redirect:/home";
		}
	  }
}
