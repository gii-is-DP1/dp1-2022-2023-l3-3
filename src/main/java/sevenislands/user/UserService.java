package sevenislands.user;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;

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
	public void save(User user) throws IllegalArgumentException {
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
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	@Transactional
	public void deleteUser(String nickname) {
		userRepository.deleteByNickname(nickname);
	}

	/**
     * Elimina un administrador de la base de datos.
     * @param admin
     */
    @Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Transactional
	public Boolean checkUserByName(String nickname) {
		return userRepository.checkUserNickname(nickname);
	}

	@Transactional
	public Boolean checkUserByEmail(String email) {
		return userRepository.checkUserEmail(email);
	}

	@Transactional
	public List<String> findDistinctAuthorities() {
		return userRepository.findAuthorities();
	}

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
        return userRepository2.findAll(pageable);
    }

	@Transactional
	public Boolean updateUser(User user, Principal principal, User authUser, User userFoundN, User userFoundE) {
		
		String password = user.getPassword();
		user.setCreationDate(authUser.getCreationDate());
		user.setEnabled(authUser.isEnabled());
		user.setId(authUser.getId());

		if((userFoundN == null || (userFoundN != null && userFoundN.getId().equals(authUser.getId()))) &&
		(userFoundE == null || (userFoundE != null && userFoundE.getId().equals(authUser.getId()))) &&
		checkEmail(user.getEmail()) &&
		password.length()>=8) {
			user.setAvatar(authUser.getAvatar());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return true;
		} else return false;
	}

	@Transactional
	public Boolean editUser(Integer id, User user, User userEdited, User userFoundN, User userFoundE) {
		String password = user.getPassword();
		user.setCreationDate(userEdited.getCreationDate());
		user.setId(userEdited.getId());
		if((userFoundN == null || (userFoundN != null && userFoundN.getId().equals(userEdited.getId()))) &&
		(userFoundE == null || (userFoundE != null && userFoundE.getId().equals(userEdited.getId()))) &&
		checkEmail(user.getEmail()) &&
		password.length()>=8) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			save(user);
			return true;
		} else return false;
	}

	@Transactional
	public Boolean enableUser(Integer id, Principal principal) {
		User user = findUser(id);
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
			info.expireNow(); //Termina la sesión
		}
		if(user.getNickname().equals(principal.getName())){
			deleteUser(id);
			return false;
		}else{
			if(lobbyService.checkUserLobbyByName(user.getId())) {
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

	@Transactional
	public Boolean addUser(User user) throws IllegalArgumentException {
		if(!checkUserByName(user.getNickname()) &&
		!checkUserByEmail(user.getEmail()) &&
		checkEmail(user.getEmail()) &&
		user.getPassword().length()>=8) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setCreationDate(new Date(System.currentTimeMillis()));
			user.setEnabled(true);
			if(user.getUserType().equals("admin")) user.setAvatar("adminAvatar.png");
			else user.setAvatar("playerAvatar.png");
			save(user);
			} catch (Exception e) {
				throw e;
			}
			return true;
		} else return false;
	}

	/**
     * Comprueba si el email pasado como parámetro es válido, es decir que cumpla el patrón "_@_._"
     * @param email
     * @return false (en caso de que el email no sea válido) o true (en caso de que sí lo sea)
     */
    public Boolean checkEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(regexPattern);
    }
}
