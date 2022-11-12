package org.springframework.samples.sevenislands.user;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.admin.Admin;
import org.springframework.samples.sevenislands.admin.AdminService;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	private static final String VIEWS_PLAYER_UPDATE_FORM = "players/updatePlayerForm";

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
				Admin admin = new Admin();
				admin.setId(authUser.getId());
				admin.setNickname(user.getNickname());
				admin.setPassword(user.getPassword());
				admin.setEnabled(authUser.isEnabled());
				admin.setFirstName(user.getFirstName());
				admin.setLastName(user.getLastName());
				admin.setEmail(user.getEmail());
				admin.setCreationDate(authUser.getCreationDate());
				admin.setBirthDate(user.getBirthDate());
				admin.setAvatar(authUser.getAvatar());
				adminService.save(admin);
			} else {
				Player player = new Player();
				player.setId(authUser.getId());
				player.setNickname(user.getNickname());
				player.setPassword(user.getPassword());
				player.setEnabled(authUser.isEnabled());
				player.setFirstName(user.getFirstName());
				player.setLastName(user.getLastName());
				player.setEmail(user.getEmail());
				player.setCreationDate(authUser.getCreationDate());
				player.setBirthDate(user.getBirthDate());
				player.setAvatar(authUser.getAvatar());
				playerService.save(player);
			}
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword());
    		Authentication authentication = authenticationManager.authenticate(authToken);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
			return "redirect:/home";
		}
	}
}
