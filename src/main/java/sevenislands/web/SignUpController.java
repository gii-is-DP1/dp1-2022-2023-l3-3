package sevenislands.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
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
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;

	@Autowired
	public SignUpController (PlayerService playerService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
		this.playerService = playerService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public String signup(Map<String, Object> model) {
		model.put("player", new Player());
		return VIEWS_PLAYER_SIGNUP;
	}

	@PostMapping
	public String processCreationForm(HttpServletRequest request, @Valid Player player, BindingResult result) {
		if(result.hasErrors()) {
			return VIEWS_PLAYER_SIGNUP;
		} else {
			String password = player.getPassword();
			player.setPassword(passwordEncoder.encode(password));
			playerService.saveNewPlayer(player);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(player.getNickname(), password);
    		authToken.setDetails(new WebAuthenticationDetails(request));
    		Authentication authentication = authenticationManager.authenticate(authToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home";
		}
	}
}