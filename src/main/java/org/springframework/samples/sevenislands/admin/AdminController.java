package org.springframework.samples.sevenislands.admin;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.lobby.LobbyService;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    
    private static final String VIEWS_CONTROL_PANEL = "views/controlPanel";

    private final UserService userService;
	private final PlayerService playerService;
	private final AdminService adminService;
	private final LobbyService lobbyService;

    @Autowired
	public AdminController(LobbyService lobbyService,UserService userService, PlayerService playerService, AdminService adminService) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.lobbyService = lobbyService;
	}

    @GetMapping("/controlPanel")
	public ModelAndView listUsers(Principal principal, HttpServletResponse response){
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
			userService.deleteUser(id);
			return "redirect:/";
		}else{
			if(userService.checkUserLobbyByName(user.getNickname())!=null) {
				Player player = playerService.findPlayersById(id);
				Lobby Lobby=lobbyService.findLobbyByPlayer(id);
				List<Player> players=Lobby.getPlayerInternal();
				players.remove(player);
				Lobby.setPlayers(players);
				lobbyService.update(Lobby);
				player.setLobby(null);
			}
			userService.deleteUser(id);
			return "redirect:/controlPanel";
		}	
	}

	@GetMapping("/controlPanel/enable/{id}")
	public String enableUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUserById(id).get();
		if(user.isEnabled()) {
			user.setEnabled(false);
			userService.update(user);
			if(user.getNickname().equals(principal.getName())){
				return "redirect:/";
			} else {
				return "redirect:/controlPanel";
			}
		} else {
			user.setEnabled(true);
			userService.update(user);
			if(user.getNickname().equals(principal.getName())){
				return "redirect:/";
			} else {
				return "redirect:/controlPanel";
			}
		}
	}

	@GetMapping("/controlPanel/add")
	public ModelAndView addUser() {
		ModelAndView result = new ModelAndView("views/addUser");
		result.addObject("user", new User());
		result.addObject("types", Arrays.asList("admin","player"));
		return result;
	}

	@PostMapping("/controlPanel/add")
	public String processCreationUserForm(@Valid User user) {
		if(user.getUserType().equals("admin")){
			Admin admin = new Admin();
			admin.setAvatar(user.getAvatar());
			admin.setBirthDate(user.getBirthDate());
			admin.setEmail(user.getEmail());
			admin.setFirstName(user.getFirstName());
			admin.setLastName(user.getLastName());
			admin.setNickname(user.getNickname());
			admin.setPassword(user.getPassword());
			adminService.save(admin);
		} else {
			Player player = new Player();
			player.setAvatar(user.getAvatar());
			player.setBirthDate(user.getBirthDate());
			player.setEmail(user.getEmail());
			player.setFirstName(user.getFirstName());
			player.setLastName(user.getLastName());
			player.setNickname(user.getNickname());
			player.setPassword(user.getPassword());
			playerService.save(player);
		}
		return "redirect:/controlPanel/add";
	}
}
