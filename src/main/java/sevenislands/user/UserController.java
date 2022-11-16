package sevenislands.user;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.exceptions.NotExitPlayerException;
import sevenislands.tools.checkers;
import sevenislands.tools.entityAssistant;

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
public class UserController {

	private static final String VIEWS_PLAYER_UPDATE_FORM = "views/updateUserForm";

	private final UserService userService;
	private final LobbyService lobbyService;
	private PasswordEncoder passwordEncoder;
	private SessionRegistry sessionRegistry;

	@Autowired
	public UserController(LobbyService lobbyService, SessionRegistry sessionRegistry, PasswordEncoder passwordEncoder, UserService userService) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.sessionRegistry = sessionRegistry;
		this.lobbyService = lobbyService;
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
		String password = user.getPassword();
		if(userService.updateUser(model, user, principal, result)) {
			//Cambia las credenciales(token) a las credenciales actualizadas
			entityAssistant.loginUser(user, password); 
			return "redirect:/home";
		}
		return VIEWS_PLAYER_UPDATE_FORM;
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
    @RequestMapping(value = "/controlPanel", method = RequestMethod.GET)
	public String listUsersPagination(Model model, @RequestParam Integer valor) throws NotExitPlayerException{
		Page<User> paginacion=null;
		Integer totalUsers=(userService.findAllUser().size())/5;
		Pageable page2=PageRequest.of(valor,5) ;
		paginacion=userService.findAllUser(page2);
		model.addAttribute("paginas", totalUsers);
		model.addAttribute("valores", valor);	
		model.addAttribute("users", paginacion.get().collect(Collectors.toList()));
		model.addAttribute("paginacion", paginacion);
		return "admin/controlPanel";
	}

	/**
	 * Ruta intermedia para la eliminación de un usuario por su id.
	 * <p> A esta ruta se llega mediante la página de panel de control al pulsar en el icono de eliminar de un
	 * usuario concreto.
	 * @param principal
	 * @param id
	 * @return
	 */
    @GetMapping("/controlPanel/delete/{id}")
	public String deleteUser(Principal principal, @PathVariable("id") Integer id){
		User user = userService.findUser(id);
		List<SessionInformation> infos = sessionRegistry.getAllSessions(user.getNickname(), false);
		for(SessionInformation info : infos) {
			info.expireNow(); //expire the session
		}

		if(user.getNickname().equals(principal.getName())){
			userService.deleteUser(id);
			return "redirect:/";
		}else{
			if(lobbyService.checkUserLobbyByName(user.getId())) {
				//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
				Lobby Lobby = lobbyService.findLobbyByPlayer(id).get();
				List<User> userList = Lobby.getPlayerInternal();
				userList.remove(user);
				Lobby.setUsers(userList);
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
	@GetMapping("/controlPanel/enable/{id}")
	public String enableUser(Principal principal, @PathVariable("id") Integer id){
		if(userService.enableUser(id, principal)) return "redirect:/controlPanel?valor=0";
		return "redirect:/";
	}

	/**
	 * Ruta de la página para añadir usuarios nuevos.
	 * @param model
	 * @return
	 */
	@GetMapping("/controlPanel/add")
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
	@PostMapping("/controlPanel/add")
	public String processCreationUserForm(Map<String, Object> model, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/controlPanel/add";
		} else if(!userService.checkUserByName(user.getNickname()) &&
				!userService.checkUserByEmail(user.getEmail()) &&
				checkers.checkEmail(user.getEmail()) &&
				user.getPassword().length()>=8) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setCreationDate(new Date(System.currentTimeMillis()));
			user.setEnabled(true);
			if(user.getUserType().equals("admin")){
				user.setAvatar("adminAvatar.png");
				userService.save(user);
			} else {
				user.setAvatar("playerAvatar.png");
				userService.save(user);
			}
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
	@GetMapping("/controlPanel/edit/{id}")
	public String editUser(@PathVariable Integer id, Map<String, Object> model) {
		User user = userService.findUser(id);
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
	@PostMapping("/controlPanel/edit/{id}")
	public String processEditUserForm(Map<String, Object> model, @PathVariable Integer id, @Valid User user, BindingResult result) {
		if(userService.editUser(model, id, user, result)) return "redirect:/controlPanel?valor=0";
		return "admin/editUser";
	}

}
