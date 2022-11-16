package sevenislands.user;


import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.tools.checkers;
import sevenislands.tools.entityAssistant;

@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private UserRepository2 userRepository2;
	private SessionRegistry sessionRegistry;
	private LobbyService lobbyService;

	@Autowired
	public UserService(LobbyService lobbyService, SessionRegistry sessionRegistry, UserRepository2 userRepository2, PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository2 = userRepository2;
		this.sessionRegistry = sessionRegistry;
		this.lobbyService = lobbyService;
	}

	@Transactional
	public void save(User user) throws DataAccessException {
		userRepository.save(user);		
	}

	@Transactional(readOnly = true)
	public User findUser(Integer id) {
		Optional<User> user = userRepository.findById(id);
		return user.isPresent()?user.get():null;
	}

	@Transactional(readOnly = true)
	public User findUser(String nickname) {
		Optional<User> user = userRepository.findByNickname(nickname);
		return user.isPresent()?user.get():null;
	}

	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.isPresent()?user.get():null;
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	@Transactional
	public void deleteUser(String nickname) {
		userRepository.delete(nickname);
	}

	@Transactional
	public Boolean checkUserByName(String nickname) {
		return userRepository.checkUser(nickname);
	}

	@Transactional
	public Boolean checkUserByEmail(String email) {
		return userRepository.checkUserEmail(email);
	}

	@Transactional 
	public void update(User user) {
	    userRepository.updateUser(user, user.getId());
	}

	@Transactional
	public List<String> findDistinctAuthorities() {
		return userRepository.findAuthorities();
	}

	@Transactional
	public List<User> findAllUser() {
		return userRepository.findAll();
	}

	/**
     * Encuentra un admin dado su id.
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> findAdmin(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Guarda un usuario nuevo.
     * <p> Esta función es necesaria ya que le establece el avatar por defecto y su fecha de cración.
     * @param admin
     * @throws DataAccessException
     */
    @Transactional
	public void saveNewAdmin(User user) throws DataAccessException {
        user.setEnabled(true);
        user.setAvatar("adminAvatar.png");
		user.setCreationDate(new Date(System.currentTimeMillis()));
		userRepository.save(user);
		//userService.save(admin);
	}

    /**
     * Elimina un administrador de la base de datos.
     * @param admin
     */
    @Transactional
	public void remove(User user) {
		userRepository.delete(user);
	}

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
        return userRepository2.findAll(pageable);
    }

	@Transactional
	public Boolean updateUser(Map<String, Object> model, User user, Principal principal, BindingResult result) {
		if (result!=null && result.hasErrors()) {
			return false;
		} else {
			User authUser = findUser(principal.getName());
			String password = user.getPassword();
			user.setCreationDate(authUser.getCreationDate());
			user.setEnabled(authUser.isEnabled());
			user.setId(authUser.getId());

			User userFoundN = findUser(user.getNickname());
			User userFoundE = findUserByEmail(user.getEmail());
			if((userFoundN == null || (userFoundN != null && userFoundN.getId().equals(authUser.getId()))) &&
			(userFoundE == null || (userFoundE != null && userFoundE.getId().equals(authUser.getId()))) &&
			checkers.checkEmail(user.getEmail()) &&
			password.length()>=8) {
				user.setAvatar(authUser.getAvatar());
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userRepository.save(user);
				return true;
			} else {
				user.setPassword("");
				List<String> errors = new ArrayList<>();
				if(userFoundN != null && !userFoundN.getId().equals(authUser.getId())) errors.add("El nombre de usuario ya está en uso.");
				if(password.length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
				if(userFoundE != null && !userFoundE.getId().equals(authUser.getId())) errors.add("El email ya está en uso.");
				if(!checkers.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
				
				model.put("errors", errors);
				return false;
			}
		}
	}

	@Transactional
	public Boolean editUser(Map<String, Object> model, Integer id, User user, BindingResult result) {
		if(result!=null && result.hasErrors()) {
			System.out.println(result.getFieldErrors());
			return false;
		} else {
			User userEdited = userRepository.findById(id).get();
			String password = user.getPassword();
			
			user.setCreationDate(userEdited.getCreationDate());
			user.setId(userEdited.getId());
			User userFoundN = findUser(user.getNickname());
			User userFoundE = findUserByEmail(user.getEmail());
			System.out.println(password);
			if((userFoundN == null || (userFoundN != null && userFoundN.getId().equals(userEdited.getId()))) &&
			(userFoundE == null || (userFoundE != null && userFoundE.getId().equals(userEdited.getId()))) &&
			checkers.checkEmail(user.getEmail()) &&
			password.length()>=8) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				save(user);
				return true;
			} else {
				List<String> errors = new ArrayList<>();
				if(userFoundN != null && !userFoundN.getId().equals(userEdited.getId())) errors.add("El nombre de usuario ya está en uso.");
				if(user.getPassword().length()<8) errors.add("La contraseña debe tener al menos 8 caracteres");
				if(userFoundE != null && !userFoundE.getId().equals(userEdited.getId())) errors.add("El email ya está en uso.");
				if(!checkers.checkEmail(user.getEmail())) errors.add("Debe introducir un email válido.");
				user.setPassword("");
				model.put("errors", errors);
				model.put("enabledValues", List.of(Boolean.valueOf(user.isEnabled()).toString(), Boolean.valueOf(!user.isEnabled()).toString()));
				return false;
			}
		}
	}

	@Transactional
	public Boolean enableUser(Integer id, Principal principal) {
		User user = userRepository.findById(id).get();
		if(user.isEnabled()) {
			user.setEnabled(false);
			save(user);
			if(user.getNickname().equals(principal.getName())) return false;
			return true;
		} else {
			user.setEnabled(true);
			save(user);
			if(user.getNickname().equals(principal.getName())) return false;
			return true;
		}
	}

	@Transactional
	public Boolean deleteUser(Integer id, Principal principal) {
		User user = findUser(id);
		List<SessionInformation> infos = sessionRegistry.getAllSessions(user.getNickname(), false);
		for(SessionInformation info : infos) {
			info.expireNow(); //expire the session
		}
		if(user.getNickname().equals(principal.getName())){
			deleteUser(id);
			return false;
		}else{
			if(lobbyService.checkUserLobbyByName(user.getId())) {
				//TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
				Lobby Lobby = lobbyService.findLobbyByPlayer(id).get();
				List<User> userList = Lobby.getPlayerInternal();
				userList.remove(user);
				Lobby.setUsers(userList);
				lobbyService.save(Lobby);
			}
			deleteUser(id);
			return true;
		}	
	}
}
