package sevenislands.user;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
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
	public void save(User user) throws IllegalArgumentException {
		userRepository.save(user);		
	}

	@Transactional(readOnly = true)
	public User findUser(Integer id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	@Transactional(readOnly = true)
	public User findUserByNickname(String nickname) {
		Optional<User> user = userRepository.findByNickname(nickname);
		return user.orElse(null);
	}

	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElse(null);
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
	public Boolean checkUserByNickname(String nickname) {
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



	/**
     * Comprueba si el email pasado como parámetro es válido, es decir que cumpla el patrón "_@_._"
     * @param email
     * @return false (en caso de que el email no sea válido) o true (en caso de que sí lo sea)
     */
    public Boolean checkEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(regexPattern);
    }

	/**
     * Comprueba si un usuario existe en la base de datos o si está baneado.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false (en otro caso)
     * @throws ServletException
     */
    public Boolean checkUserNoExists(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(!checkUserByNickname(principal.getUsername()) || !findUserByNickname(principal.getUsername()).isEnabled()) {
            request.getSession().invalidate();
            request.logout();
            return true;
        } else return false;
    }

	/**
     * Comprueba que el usuario existe en la base de datos y que no está baneado.
     * <p> En este caso, si el usuario estaba en una lobby es expulsado.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (si está baneado o no se encuentra en la base de datos) o false (en otro caso)
     * @throws ServletException
     */
    public Boolean checkUser(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(checkUserByNickname(principal.getUsername()) && findUserByNickname(principal.getUsername()).isEnabled()) {
            User user = findUserByNickname(principal.getUsername());
            if (lobbyService.checkUserLobbyByName(user.getId())) {
                //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
                Lobby lobby = lobbyService.findLobbyByPlayer(user.getId()).get();
                List<User> users = lobby.getPlayerInternal();
                if (users.size() == 1) {
                    lobby.setActive(false);
                }
                users.remove(user);
                lobby.setUsers(users);
                lobbyService.update(lobby);
            }
            return false;
        } else {
            request.getSession().invalidate();
            request.logout();
            return true;
        }
    }

	@Transactional
	public void addUser(User user, Boolean isAdmin, AuthenticationManager authenticationManager, HttpServletRequest request){
		try {
			String password = user.getPassword();
			if(password.length() < 8){
				throw new IllegalArgumentException("Contraseña no válida, longitud mínima de contraseña = 8");
			} else if(!checkEmail(user.getEmail())){
				throw new IllegalArgumentException("Email no válido, formato no válido"); 
			}
			user.setPassword(passwordEncoder.encode(password));
			
			user.setCreationDate(new Date(System.currentTimeMillis()));
			user.setEnabled(true);
			
			

			if(user.getUserType().equals("player")){
				user.setAvatar("playerAvatar.png");
				

			} else if(user.getUserType().equals("admin")){
				user.setAvatar("adminAvatar.png");
			}
			save(user);
			if(!isAdmin){
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), password);
				Authentication authentication = authenticationManager.authenticate(authToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				authToken.setDetails(new WebAuthenticationDetails(request));
			}
		} catch (Exception e) {
			throw e;
		}
	}



	@Transactional
	public void updateUser(User newUserData, String param, Integer op){
		User oldUser;
		if(newUserData.getPassword().length() < 8){
			throw new IllegalArgumentException("Contraseña no válida, longitud mínima de contraseña = 8");
		} else if(!checkEmail(newUserData.getEmail())){
			throw new IllegalArgumentException("Email no válido, formato no válido"); 
		}
		try {
			switch (op) {
				case 0: // quiero por id
					oldUser = userRepository.findById(Integer.valueOf(param)).orElse(null);
					break;
				case 1: // por email
					oldUser = userRepository.findByEmail(param).orElse(null);
					break;
				case 2:  // por nickname
					oldUser = userRepository.findByNickname(param).orElse(null);
					break;
				default:
					oldUser = userRepository.findById(Integer.valueOf(param)).orElse(null);
					break;
			}
			oldUser.setNickname(newUserData.getNickname());
			oldUser.setEmail(newUserData.getEmail());
			oldUser.setBirthDate(newUserData.getBirthDate());
			oldUser.setFirstName(newUserData.getFirstName());
			oldUser.setLastName(newUserData.getLastName());
			oldUser.setPassword(passwordEncoder.encode(newUserData.getPassword()));
			if(op.equals(3)) {oldUser.setEnabled(newUserData.isEnabled());}
			save(oldUser);
		} catch (Exception e) {
			throw e;
		}
	}
}
