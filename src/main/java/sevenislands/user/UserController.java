package sevenislands.user;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.admin.AdminService;
import sevenislands.player.PlayerService;
import sevenislands.tools.methods;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	private static final String VIEWS_PLAYER_UPDATE_FORM = "views/updateUserForm";

	private final UserService userService;
	private final PlayerService playerService;
	private final AdminService adminService;
	private AuthenticationManager authenticationManager;

	@Autowired
	public UserController(UserService userService, PlayerService playerService, AdminService adminService, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/settings")
	public String initUpdateOwnerForm(Map<String, Object> model, Principal principal) {
		User user = userService.findUser(principal.getName()).get();
		model.put("user", user);
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@PostMapping("/settings")
	public String processUpdateplayerForm(@Valid User user, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		} else {
			User authUser = userService.findUser(principal.getName()).get();
			if(authUser.getUserType().equals("admin")){
				adminService.save(methods.parseAdmin(user));
			} else {
				playerService.save(methods.parsePlayer(user));
			}
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword());
    		Authentication authentication = authenticationManager.authenticate(authToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home";
		}
	}
}
