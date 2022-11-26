package sevenislands.user;

import java.security.Principal;
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

import sevenislands.exceptions.NotExitPlayerException;
import sevenislands.tools.entityAssistant;

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

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/settings")
	public String initUpdateOwnerForm(HttpServletRequest request, Map<String, Object> model, Principal principal) throws ServletException {
		if(userService.checkUser(request)) return "redirect:/";
		User user = userService.findUserByNickname(principal.getName());
		user.setPassword("");
		model.put("user", user);
		return VIEWS_PLAYER_UPDATE_FORM;
	}

	@PostMapping("/settings")
	public String processUpdateplayerForm(Map<String, Object> model, @Valid User user, BindingResult result, Principal principal) {
		String password = user.getPassword();
		if(result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		}
		try {
			userService.updateUser(user, principal.getName(), 2);
			entityAssistant.loginUser(user, password); 
			return "redirect:/home";
		} catch (Exception e) {
			List<String> errors = new ArrayList<>();
			if(e.getMessage().contains("PUBLIC.USER(NICKNAME)")) {
				errors.add("El nombre de usuario ya esta en uso");
			}
			else if (e.getMessage().contains("PUBLIC.USER(EMAIL)")){
				errors.add("El email ya esta en uso");
			} else {
				errors.add(e.getMessage());
			}
			user.setPassword("");
			model.put("errors", errors);
			return VIEWS_PLAYER_UPDATE_FORM;
		}
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
		Integer totalUsers=(userService.findAll().size())/5;
		Pageable page2=PageRequest.of(valor,5);
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
		if(userService.deleteUser(id, principal)) return "redirect:/controlPanel?valor=0";
		return "redirect:/";
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
		if(result.hasErrors()) return "admin/addUser";
		
		try {
			userService.addUser(user, true, null, null);
			 return "redirect:/controlPanel/add";
		} catch (Exception e) {
			user.setPassword("");
			List<String> errors = new ArrayList<>();
			if(e.getMessage().contains("PUBLIC.USER(NICKNAME)")) {
				errors.add("El nombre de usuario ya esta en uso");
			} else if (e.getMessage().contains("PUBLIC.USER(EMAIL)")){
				errors.add("El email ya esta en uso");
			} else {
				errors.add(e.getMessage());
			}		
			model.put("types", userService.findDistinctAuthorities());
			model.put("errors", errors);
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
		if(result.hasErrors()) {
			return "admin/editUser";
		}
		try {
			userService.updateUser(user, id.toString(), 3);
			return "redirect:/controlPanel?valor=0";
		} catch (Exception e) {
			List<String> errors = new ArrayList<>();
			if(e.getMessage().contains("PUBLIC.USER(NICKNAME)")) {
				errors.add("El nombre de usuario ya esta en uso");
			} else if (e.getMessage().contains("PUBLIC.USER(EMAIL)")){
				errors.add("El email ya esta en uso");
			} else {
				errors.add(e.getMessage());
			}
			user.setPassword("");
			model.put("errors", errors);
			model.put("enabledValues", List.of(Boolean.valueOf(user.isEnabled()).toString(), Boolean.valueOf(!user.isEnabled()).toString()));
			return "admin/editUser";
		}
		
	}

}
