package sevenislands.web;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

	private static final String VIEWS_PLAYER_SIGNUP = "views/signup";

	private final UserService userService;
	private AuthenticationManager authenticationManager;

	@Autowired
	public SignUpController (UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@GetMapping
	public String signup(Map<String, Object> model) {
		model.put("user", new User());
		return VIEWS_PLAYER_SIGNUP;
	}

	@PostMapping
	public String processCreationForm(Map<String, Object> model, HttpServletRequest request, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return VIEWS_PLAYER_SIGNUP;
		} 
		try {
			user.setUserType("player");
			userService.addUser(user, false, authenticationManager, request);
			return "redirect:/home";
		} catch (Exception e) {
			List<String> errors = new ArrayList<>();
			if(e.getMessage().contains("PUBLIC.USER(NICKNAME)")) {
				errors.add("El nombre de usuario ya esta en uso");
			} else if (e.getMessage().contains("PUBLIC.USER(EMAIL)")){
				errors.add("El email ya esta en uso");
			} else errors.add(e.getMessage());
			model.put("errors", errors);
			user.setPassword("");
			return VIEWS_PLAYER_SIGNUP;
		}
		
	}
}
