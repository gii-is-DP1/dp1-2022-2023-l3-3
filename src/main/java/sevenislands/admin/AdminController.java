package sevenislands.admin;

import java.security.Principal;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.exceptions.NotExitPlayerException;
import sevenislands.player.Player;
import sevenislands.player.PlayerService;
import sevenislands.tools.checkers;
import sevenislands.tools.entityAssistant;
import sevenislands.user.User;
import sevenislands.user.UserService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/controlPanel")
public class AdminController {
    
    private static final String VIEWS_CONTROL_PANEL = "admin/controlPanel";

	
	
    private final UserService userService;
	private final PlayerService playerService;
	private final AdminService adminService;
	private final LobbyService lobbyService;
	private SessionRegistry sessionRegistry;
	private PasswordEncoder passwordEncoder;
//
    @Autowired
	public AdminController(PasswordEncoder passwordEncoder, SessionRegistry sessionRegistry, LobbyService lobbyService,UserService userService, PlayerService playerService, AdminService adminService) {
		this.userService = userService;
		this.playerService = playerService;
		this.adminService = adminService;
		this.lobbyService = lobbyService;
		this.sessionRegistry = sessionRegistry;
		this.passwordEncoder = passwordEncoder;
		
		
	}
	
	/**
	 * Vista principal del panel de control del administrador.
	 * <p> Muestra un listado de todos los usuarios almacenados en la base de datos y permite la eliminación o el baneo
	 * de cualquiera de estos.
	 * @param model
	 * @param principal
	 * @param response
	 * @return
	 * @throws NotExitPlayerException
	 */
    // @GetMapping
	// public String listUsers(Map<String, Object> model, Principal principal, HttpServletResponse response) throws NotExitPlayerException{
	// 	response.addHeader("Refresh", "5");
    //     List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());      
    //     model.put("users", users);
	// 	return VIEWS_CONTROL_PANEL;
	// }
	
    @RequestMapping(method = RequestMethod.GET)
	public String listUsersPagination(Model model, @RequestParam Integer valor) throws NotExitPlayerException{
		Page<User> paginacion=null;
		Integer totalPlayer=(userService.findAllUser().size())/5;
		Pageable page2=PageRequest.of(valor,5) ;
		paginacion=adminService.findAllUser(page2);
		model.addAttribute("paginas", totalPlayer);
		model.addAttribute("valores", valor);	
		model.addAttribute("users", paginacion.get().collect(Collectors.toList()));
		model.addAttribute("paginacion", paginacion);
		return VIEWS_CONTROL_PANEL;
	}

	/**
	 * Ruta intermedia para la eliminación de un usuario por su id.
	 * <p> A esta ruta se llega mediante la página de panel de control al pulsar en el icono de eliminar de un
	 * usuario concreto.
	 * @param principal
	 * @param id
	 * @return
	 */
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
			if(lobbyService.checkUserLobbyByName(user.getId())) {
				//TODO: Poner el Player como Optional<Player> y realizar la comprobación de que existe
				Player player = playerService.findPlayer(id).get();
				//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
				Lobby Lobby = lobbyService.findLobbyByPlayer(id);
				List<Player> players=Lobby.getPlayerInternal();
				players.remove(player);
				Lobby.setPlayers(players);
				lobbyService.update(Lobby);
			}
			userService.deleteUser(id);
			return "redirect:/controlPanel?valor=0";
		}	
	}

	/**
	 * Ruta intermedia para banear/desbanear un usuario por su id.
	 * <p> A esta ruta se llega mediante la página de panel de control al pulsar en el icono de correspondiente de un
	 * usuario concreto.
	 * @param principal
	 * @param id
	 * @return
	 */
	@GetMapping("/enable/{id}")
	public String enableUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUser(id).get();
		if(user.isEnabled()) {
			user.setEnabled(false);
			userService.update(user);
			if(user.getNickname().equals(principal.getName())){
				return "redirect:/";
			} else {
				return "redirect:/controlPanel?valor=0";
			}
		} else {
			user.setEnabled(true);
			userService.update(user);
			if(user.getNickname().equals(principal.getName())){
				return "redirect:/";
			} else {
				return "redirect:/controlPanel?valor=0";
			}
		}
	}

	/**
	 * Ruta de la página para añadir usuarios nuevos.
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String addUser(Map<String, Object> model) {
		model.put("user", new User());
		model.put("types", userService.findDistinctAuthorities());
		return "admin/addUser";
	}

	/**
	 * Ruta que gestiona el proceso de creación de un usuario nuevo.
	 * <p> Realiza todas las comprobaciones y se asegura de que el usuario que se quiere crear se haga de forma corecta.
	 * @param model
	 * @param user
	 * @param result
	 * @return
	 */
	@PostMapping("/add")
	public String processCreationUserForm(Map<String, Object> model, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/controlPanel/add";
		} else if(!userService.checkUserByName(user.getNickname()) &&
				!userService.checkUserByEmail(user.getEmail()) &&
				checkers.checkEmail(user.getEmail()) &&
				user.getPassword().length()>=8) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			if(user.getUserType().equals("admin")){
				adminService.saveNewAdmin(entityAssistant.parseAdmin(user));
			} else playerService.saveNewPlayer(entityAssistant.parsePlayer(user));
			return "redirect:/controlPanel/add";
		} else {
			user.setPassword("");
			List<String> errors = new ArrayList<>();
			if(userService.checkUserByName(user.getNickname())) errors.add("El nombre de usuario ya está en uso.");
			if(user.getPassword().length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
			if(userService.checkUserByEmail(user.getEmail())) errors.add("El email ya está en uso.");
			if(!checkers.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
			model.put("errors", errors);
			model.put("types", userService.findDistinctAuthorities());
			return "admin/addUser";
		}
	}

	/**
	 * Ruta de la página para editar un usuario concreto por su id.
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable Integer id, Map<String, Object> model) {
		User user = userService.findUser(id).get();
		List<String> authList = userService.findDistinctAuthorities();
		user.setPassword("");
		authList.remove(user.getUserType());
		authList.add(0, user.getUserType());
		model.put("user", user);
		model.put("types", authList);
		model.put("enabledValues", List.of(Boolean.valueOf(user.isEnabled()).toString(), Boolean.valueOf(!user.isEnabled()).toString()));
		return "admin/editUser";
	}

	/**
	 * Ruta que gestiona la edición de los datos del usuario.
	 * <p> Realiza todas las comprobaciones y se asegura de que la edición del usuario se haga de forma corecta.
	 * @param model
	 * @param id
	 * @param user
	 * @param result
	 * @return
	 */
	@PostMapping("/edit/{id}")
	public String processEditUserForm(Map<String, Object> model, @PathVariable Integer id, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			System.out.println(result.getFieldErrors());
			return "àdmin/edit/";
		} else {
			User userEdited = userService.findUser(id).get();
			String password = user.getPassword();
			
			user.setCreationDate(userEdited.getCreationDate());
			user.setId(userEdited.getId());
			Optional<User> userFoundN = userService.findUser(user.getNickname());
			Optional<User> userFoundE = userService.findUserByEmail(user.getEmail());
			System.out.println(password);
			if((!userFoundN.isPresent() || (userFoundN.isPresent() && userFoundN.get().getId().equals(userEdited.getId()))) &&
			(!userFoundE.isPresent() || (userFoundE.isPresent() && userFoundE.get().getId().equals(userEdited.getId()))) &&
			checkers.checkEmail(user.getEmail()) &&
			password.length()>=8) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				if(userEdited.getUserType().equals("admin")) {
					adminService.save(entityAssistant.parseAdmin(user));
				} else playerService.save(entityAssistant.parsePlayer(user));
				return "redirect:/controlPanel?valor=0";
			} else {
				List<String> errors = new ArrayList<>();
				if(userFoundN.isPresent() && !userFoundN.get().getId().equals(userEdited.getId())) errors.add("El nombre de usuario ya está en uso.");
				if(user.getPassword().length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
				if(userFoundE.isPresent() && !userFoundE.get().getId().equals(userEdited.getId())) errors.add("El email ya está en uso.");
				if(!checkers.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
				user.setPassword("");
				model.put("errors", errors);
				model.put("enabledValues", List.of(Boolean.valueOf(user.isEnabled()).toString(), Boolean.valueOf(!user.isEnabled()).toString()));
				return "admin/editUser";
			}
		}
	}
}
