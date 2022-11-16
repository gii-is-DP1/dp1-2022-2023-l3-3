package sevenislands.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.admin.AdminService;
import sevenislands.player.PlayerService;
import sevenislands.tools.checkers;
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
	public String initUpdateOwnerForm(HttpServletRequest request, Map<String, Object> model, Principal principal) throws ServletException {
		if(checkers.checkUser(request)) return "redirect:/";
		User user = userService.findUser(principal.getName());
		user.setPassword("");
		model.put("user", user);
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@PostMapping("/settings")
	public String processUpdateplayerForm(Map<String, Object> model, @Valid User user, BindingResult result, Principal principal) {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		} else {
			User authUser = userService.findUser(principal.getName());
			String password = user.getPassword();
			user.setCreationDate(authUser.getCreationDate());
			user.setEnabled(authUser.isEnabled());
			user.setId(authUser.getId());

			User userFoundN = userService.findUser(user.getNickname());
			User userFoundE = userService.findUserByEmail(user.getEmail());

			if((userFoundN == null || (userFoundN != null && userFoundN.getId().equals(authUser.getId()))) &&
			(userFoundE == null || (userFoundE != null && userFoundE.getId().equals(authUser.getId()))) &&
			checkers.checkEmail(user.getEmail()) &&
			password.length()>=8) { 
				//Guardalo
				user.setAvatar(authUser.getAvatar());
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				if(authUser.getUserType().equals("admin")){
					adminService.save(entityAssistant.parseAdmin(user));
				} else playerService.save(entityAssistant.parsePlayer(user));
				//Cambia las credenciales(token) a las credenciales actualizadas
				entityAssistant.loginUser(user, password); 
				return "redirect:/home";
			} else {
				user.setPassword("");
				List<String> errors = new ArrayList<>();
				if(userFoundN != null && !userFoundN.getId().equals(authUser.getId())) errors.add("El nombre de usuario ya está en uso.");
				if(password.length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
				if(userFoundE != null && !userFoundE.getId().equals(authUser.getId())) errors.add("El email ya está en uso.");
				if(!checkers.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
				
				model.put("errors", errors);
				return VIEWS_PLAYER_UPDATE_FORM; //No me lo guardes
			}
		}
	}
}
