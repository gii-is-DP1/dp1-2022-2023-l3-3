package sevenislands.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import sevenislands.exceptions.NotExitPlayerException;
import sevenislands.tools.metodosReutilizables;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public String initUpdateOwnerForm(HttpServletRequest request, ModelMap model, @ModelAttribute("logedUser") User logedUser) throws ServletException {
		try {
			userService.checkUser(request, logedUser);
			logedUser.setPassword("");
			model.put("user", logedUser);
			return VIEWS_PLAYER_UPDATE_FORM;
		} catch (Exception e) {
			return "redirect:/";
		}
		
	}

	@PostMapping("/settings")
	public String processUpdateplayerForm(HttpServletRequest request, ModelMap model, @Valid User user, BindingResult result, @ModelAttribute("logedUser") User logedUser) {
		String password = user.getPassword();
		if(result.hasErrors()) {
			return VIEWS_PLAYER_UPDATE_FORM;
		}
		try {
			userService.updateUser(user, logedUser.getNickname(), 2);
			userService.loginUser(user, password, request); 
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
	public String listUsersPagination(ModelMap model, @RequestParam Integer valor) throws NotExitPlayerException{
		Page<User> paginacion=null;
		Integer totalUsers=(userService.findAll().size()-1)/5;
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
	
    @GetMapping("/controlPanel/delete/{idUserDeleted}")
	public String deleteUser(@ModelAttribute("logedUser") User logedUser, @PathVariable("idUserDeleted") Integer id){
		try {
			if(userService.deleteUser(id, logedUser)|| logedUser.getUserType().equals("admin")) return "redirect:/controlPanel?valor="+metodosReutilizables.DeletePaginaControlPanel(id);
			else return "redirect:/";
		} catch (Exception e) {
			return "redirect:/";
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
	@GetMapping("/controlPanel/enable/{idUserEdited}")
	public String enableUser(@ModelAttribute("logedUser") User logedUser, @PathVariable("idUserEdited") Integer id){
		if(userService.enableUser(id, logedUser)) return "redirect:/controlPanel?valor="+metodosReutilizables.EditPaginaControlPanel(id);
		return "redirect:/";
	}

	/**
	 * Ruta de la página para añadir usuarios nuevos.
	 * @param model
	 * @return
	 */
	@GetMapping("/controlPanel/add")
	public String addUser(ModelMap model) {
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
	public String processCreationUserForm(ModelMap model, @Valid User user, BindingResult result) {
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
	@GetMapping("/controlPanel/edit/{idUserEdited}")
	public String editUser(@PathVariable("idUserEdited") Integer id, ModelMap model) {
		Optional<User> userEdited = userService.findUserById(id);
		if(userEdited.isPresent()) {
			List<String> authList = userService.findDistinctAuthorities();
			userEdited.get().setPassword("");
			authList.remove(userEdited.get().getUserType());
			authList.add(0, userEdited.get().getUserType());
			model.put("user", userEdited.get());
			model.put("types", authList);
			model.put("enabledValues", List.of(Boolean.valueOf(userEdited.get().isEnabled()).toString(), Boolean.valueOf(!userEdited.get().isEnabled()).toString()));
			return "admin/editUser";
		} else return "redirect:/controlPanel/?valor=0";
		
	}

	/**
	 * Ruta que gestiona la edición de los datos del usuario.
	 * <p> Realiza todas las comprobaciones y se asegura de que la edición del usuario se haga de forma correcta.
	 * @param model
	 * @param id
	 * @param user
	 * @param result
	 * @return
	 */
	@PostMapping("/controlPanel/edit/{idUserEdited}")
	public String processEditUserForm(ModelMap model, @PathVariable("idUserEdited") Integer id, @Valid User user, BindingResult result) {
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
