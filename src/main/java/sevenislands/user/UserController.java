package sevenislands.user;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.admin.AdminService;
import sevenislands.player.PlayerService;
import sevenislands.tools.entityAssistant;

import org.springframework.security.crypto.password.PasswordEncoder;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(PasswordEncoder passwordEncoder, UserService userService, PlayerService playerService, AdminService adminService) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/settings")
	public String initUpdateOwnerForm(Map<String, Object> model, Principal principal) {
		User user = userService.findUser(principal.getName()).get();
		user.setPassword("");
		model.put("user", user);
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@PostMapping("/settings")
	public String processUpdateplayerForm(@Valid User user, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		} else {
			User authUser = userService.findUser(principal.getName()).get();
			String password = user.getPassword();
			user.setCreationDate(authUser.getCreationDate());
			user.setEnabled(authUser.isEnabled());
			user.setId(authUser.getId());
			user.setAvatar(authUser.getAvatar());
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			Optional<User> userFoundN = userService.findUser(user.getNickname());
			Optional<User> userFoundE = userService.findUserByEmail(user.getEmail());

			if((!userFoundN.isPresent() || (userFoundN.isPresent() && userFoundN.get().getId().equals(authUser.getId()))) &&
			(!userFoundE.isPresent() || (userFoundE.isPresent() && userFoundE.get().getId().equals(authUser.getId())))) { 
				//Guardalo
				if(authUser.getUserType().equals("admin")){
					adminService.save(entityAssistant.parseAdmin(user));
				} else playerService.save(entityAssistant.parsePlayer(user));
				//Cambia las credenciales(token) a las credenciales actualizadas
				entityAssistant.loginUser(user, password); 
				return "redirect:/home";
			} return "redirect:/settings"; //No me lo guardes
		}
	}
}
