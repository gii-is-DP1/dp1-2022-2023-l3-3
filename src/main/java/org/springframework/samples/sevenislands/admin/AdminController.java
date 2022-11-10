package org.springframework.samples.sevenislands.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.exceptions.NotExitPlayerException;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    
    private static final String VIEWS_CONTROL_PANEL = "views/controlPanel";

    private final UserService userService;

    @Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

    @GetMapping("/controlPanel")
	public ModelAndView listUsers(Principal principal, HttpServletResponse response) throws NotExitPlayerException{
		response.addHeader("Refresh", "5");
		ModelAndView result = new ModelAndView(VIEWS_CONTROL_PANEL);
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());      
        result.addObject("users", users);
		return result;
	}

    @GetMapping("/controlPanel/delete/{id}")
	public String deleteUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUserById(id).get();
		if(user.getNickname().equals(principal.getName())){
			user.setEnabled(false);
			userService.updatUser(user);
			return "redirect:/";
		}else{
			user.setEnabled(false);
			userService.updatUser(user);
			return "redirect:/controlPanel";
		}
	}
}
