package org.springframework.samples.sevenislands.admin;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.lobby.LobbyService;
import org.springframework.samples.sevenislands.lobby.exceptions.NotExitPlayerException;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.samples.sevenislands.player.PlayerService;
import org.springframework.samples.sevenislands.user.AuthoritiesService;
import org.springframework.samples.sevenislands.user.User;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/controlPanel")
public class AdminController {
    
    private static final String VIEWS_CONTROL_PANEL = "views/controlPanel";

    private final UserService userService;
	private final PlayerService playerService;
	private final AdminService adminService;
	private final LobbyService lobbyService;
	private final AuthoritiesService authoritiesService;
	private SessionRegistry sessionRegistry;

    @Autowired
	public AdminController(SessionRegistry sessionRegistry, LobbyService lobbyService,UserService userService, PlayerService playerService, AdminService adminService, AuthoritiesService authoritiesService) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.lobbyService = lobbyService;
		this.authoritiesService = authoritiesService;
		this.sessionRegistry = sessionRegistry;
	}

    @GetMapping
	public String listUsers(Map<String, Object> model, Principal principal, HttpServletResponse response) throws NotExitPlayerException{
		response.addHeader("Refresh", "5");
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());      
        model.put("users", users);
		return VIEWS_CONTROL_PANEL;
	}

    @GetMapping("/delete/{id}")
	public String deleteUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUser(id).get();
		List<SessionInformation> infos = sessionRegistry.getAllSessions(user.getNickname(), false);
		for(SessionInformation info : infos) {
			info.expireNow(); //expire the session
		}

		if(user.getNickname().equals(principal.getName())){
			userService.deleteUser(id);
			return "redirect:/";
		}else{
			if(userService.checkUserLobbyByName(user.getNickname())) {
				Player player = playerService.findPlayer(id);
				Lobby Lobby = lobbyService.findLobbyByPlayer(id);
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

	@GetMapping("/enable/{id}")
	public String enableUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUser(id).get();
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

	@GetMapping("/add")
	public String addUser(Map<String, Object> model) {
		model.put("user", new User());
		model.put("types", authoritiesService.findDistinctAuthorities());
		return "views/addUser";
	}

	@PostMapping("/add")
	public String processCreationUserForm(@Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/controlPanel/add";
		} else {
			if(user.getUserType().equals("admin")){
				Admin admin = new Admin();
				admin.setBirthDate(user.getBirthDate());
				admin.setEmail(user.getEmail());
				admin.setFirstName(user.getFirstName());
				admin.setLastName(user.getLastName());
				admin.setNickname(user.getNickname());
				admin.setPassword(user.getPassword());
				adminService.saveNewAdmin(admin);
			} else {
				Player player = new Player();
				player.setBirthDate(user.getBirthDate());
				player.setEmail(user.getEmail());
				player.setFirstName(user.getFirstName());
				player.setLastName(user.getLastName());
				player.setNickname(user.getNickname());
				player.setPassword(user.getPassword());
				playerService.saveNewPlayer(player);
			}
			return "redirect:/controlPanel/add";
		}
	}
}
