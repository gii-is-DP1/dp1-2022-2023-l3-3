package sevenislands.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.checkers;
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

	private final PlayerService playerService;
	private final UserService userService;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;

	@Autowired
	public SignUpController (UserService userService, PlayerService playerService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
		this.playerService = playerService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@GetMapping
	public String signup(Map<String, Object> model) {
		model.put("player", new Player());
		return VIEWS_PLAYER_SIGNUP;
	}

	@PostMapping
	public String processCreationForm(Map<String, Object> model, HttpServletRequest request, @Valid Player player, BindingResult result) {
		if(result.hasErrors()) {
			return VIEWS_PLAYER_SIGNUP;
		} else if(!userService.checkUserByName(player.getNickname()) &&
				!userService.checkUserByEmail(player.getEmail()) &&
				checkers.checkEmail(player.getEmail()) &&
				player.getPassword().length()>=8) {
			String password = player.getPassword();
			player.setPassword(passwordEncoder.encode(password));
			playerService.saveNewPlayer(player);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(player.getNickname(), password);
    		authToken.setDetails(new WebAuthenticationDetails(request));
    		Authentication authentication = authenticationManager.authenticate(authToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home";
		} else {
			List<String> errors = new ArrayList<>();
			if(userService.checkUserByName(player.getNickname())) errors.add("El nombre de usuario ya está en uso.");
			if(player.getPassword().length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
			if(userService.checkUserByEmail(player.getEmail())) errors.add("El email ya está en uso.");
			if(!checkers.checkEmail(player.getEmail())) errors.add("Debe introducir un email válido.");
			model.put("errors", errors);
			return VIEWS_PLAYER_SIGNUP;
		}
	}
}
