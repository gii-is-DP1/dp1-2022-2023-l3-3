package sevenislands.web;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.tools.checkers;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;

	@Autowired
	public SignUpController (UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
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
		} else if(!userService.checkUserByName(user.getNickname()) &&
				!userService.checkUserByEmail(user.getEmail()) &&
				userService.checkEmail(user.getEmail()) &&
				user.getPassword().length()>=8) {
			String password = user.getPassword();
			user.setPassword(passwordEncoder.encode(password));
			user.setAvatar("playerAvatar.png");
			user.setCreationDate(new Date(System.currentTimeMillis()));
			user.setEnabled(true);
			user.setUserType("player");
			userService.save(user);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), password);
    		authToken.setDetails(new WebAuthenticationDetails(request));
    		Authentication authentication = authenticationManager.authenticate(authToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home";
		} else {
			List<String> errors = new ArrayList<>();
			if(userService.checkUserByName(user.getNickname())) errors.add("El nombre de usuario ya está en uso.");
			if(user.getPassword().length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
			if(userService.checkUserByEmail(user.getEmail())) errors.add("El email ya está en uso.");
			if(!userService.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
			model.put("errors", errors);
			return VIEWS_PLAYER_SIGNUP;
		}
	}
}
