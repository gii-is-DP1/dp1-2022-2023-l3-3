package sevenislands.admin;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.exceptions.NotExitPlayerException;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.methods;
import sevenislands.user.User;
import sevenislands.user.UserService;
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
    
    private static final String VIEWS_CONTROL_PANEL = "admin/controlPanel";

    private final UserService userService;
	private final PlayerService playerService;
	private final AdminService adminService;
	private final LobbyService lobbyService;
	private SessionRegistry sessionRegistry;

    @Autowired
	public AdminController(SessionRegistry sessionRegistry, LobbyService lobbyService,UserService userService, PlayerService playerService, AdminService adminService) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.lobbyService = lobbyService;
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
		model.put("types", userService.findDistinctAuthorities());
		return "admin/addUser";
	}

	@PostMapping("/add")
	public String processCreationUserForm(@Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/controlPanel/add";
		} else {
			if(user.getUserType().equals("admin")){
				adminService.saveNewAdmin(methods.parseAdmin(user));
			} else playerService.saveNewPlayer(methods.parsePlayer(user));
			return "redirect:/controlPanel/add";
		}
	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable Integer id, Map<String, Object> model) {
		User user = userService.findUser(id).get();
		List<String> authList = userService.findDistinctAuthorities();
		authList.remove(user.getUserType());
		authList.add(0, user.getUserType());
		model.put("user", user);
		model.put("types", authList);
		model.put("enabledValues", List.of(Boolean.valueOf(user.isEnabled()).toString(), Boolean.valueOf(!user.isEnabled()).toString()));
		return "admin/editUser";
	}

	@PostMapping("/edit/{id}")
	public String processEditUserForm(@PathVariable Integer id, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			System.out.println(result.getFieldErrors());
			return "redirect:/controlPanel/edit/"+id.toString();
		} else {
			User userEdited = userService.findUser(id).get();
			user.setCreationDate(userEdited.getCreationDate());
			if(userEdited.getUserType().equals("admin")) {
				adminService.save(methods.parseAdmin(user));
			} else playerService.save(methods.parsePlayer(user));
			return "redirect:/controlPanel";
		}
	}

}
