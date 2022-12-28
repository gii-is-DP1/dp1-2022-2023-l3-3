package sevenislands.user;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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


import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;

@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private SessionRegistry sessionRegistry;
	private LobbyService lobbyService;
	private AuthenticationManager authenticationManager;

	@Autowired
	public UserService(AuthenticationManager authenticationManager, LobbyService lobbyService, SessionRegistry sessionRegistry, PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.sessionRegistry = sessionRegistry;
		this.lobbyService = lobbyService;
		this.authenticationManager = authenticationManager;
	}
	
	public User createUser(Integer id, String nickname,String email) {
        User userCreate;
        userCreate = new User();
        userCreate.setId(id);
        userCreate.setNickname(nickname);
        userCreate.setEmail(email);
        userCreate.setPassword("pass");
        userCreate.setEnabled(true);
        userCreate.setFirstName("Prueba");
        userCreate.setLastName("Probando");
		userCreate.setCreationDate(new Date(System.currentTimeMillis()));
        userCreate.setBirthDate(new Date(System.currentTimeMillis()));
        userCreate.setAvatar("resource/images/avatars/playerAvatar.png");
        userCreate.setUserType("player");

        return userCreate;
    }

	@Transactional
	public User save(User user) throws IllegalArgumentException {
		return userRepository.save(user);		
	}

	@Transactional(readOnly = true)
	public Optional<User> findUserById(Integer id) {
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUserByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	//No se usa en ningún lado
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
	public void deleteUserById(Integer id) {
		userRepository.deleteById(id);
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
	public Boolean checkUserByEmail(String email) {
		return userRepository.checkUserEmail(email);
	}

	@Transactional
	public List<String> findDistinctAuthorities() {
		return userRepository.findAuthorities();
	}

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
		List<User> users=userRepository.findAll();
		Page<User> page=null;
		if(pageable.getPageNumber()==0){
			int valor=(int)pageable.getPageSize();
			page = new PageImpl<>(users.subList(0,valor));
		}else if((pageable.getPageNumber()*5)+5>=users.size()){
			int numPag=pageable.getPageNumber();
			page = new PageImpl<>(users.subList((5*(numPag-1))+5,users.size()));
		}else{
			int numPag=pageable.getPageNumber();
			int valor=(int)pageable.getPageSize();
			page = new PageImpl<>(users.subList((5*(numPag-1))+5, valor*(numPag+1)));
		}
		
		//userRepository2.findAll(pageable)
		return page;
    }

	@Transactional
	public Boolean enableUser(Integer id, User logedUser) {
		Optional<User> userEdited = findUserById(id);
		if(userEdited.isPresent()) {
			if(userEdited.get().getNickname().equals(logedUser.getNickname())) {return false;}
			else if(userEdited.get().isEnabled()) {
				userEdited.get().setEnabled(false);
				save(userEdited.get());
			} else {
				userEdited.get().setEnabled(true);
				save(userEdited.get());
			}
		} return true;
	}

	@Transactional
	public Boolean deleteUser(Integer id, User logedUser) {
	
			Optional<User> userDeleted = findUserById(id);
		if(userDeleted.isPresent()) {
			List<SessionInformation> infos = sessionRegistry.getAllSessions(userDeleted.get().getNickname(), false);
			for(SessionInformation info : infos) {
				info.expireNow(); //Termina la sesión
			}
			try{
				Lobby lobby = lobbyService.findLobbyByPlayerId(id);
				List<User> userList = lobby.getPlayerInternal();
				userList.remove(userDeleted.get());
				lobby.setUsers(userList);
				lobbyService.save(lobby);
				deleteUserById(id);
				return true;
		}catch(NotExistLobbyException e){
			deleteUserById(id);
			return true;
		}
		} return false;
		
	}

	/**
     * Comprueba si el email pasado como parámetro es válido, es decir que cumpla el patrón "_@_._"
     * @param email
     * @return false (en caso de que el email no sea válido) o true (en caso de que sí lo sea)
     */
	@Transactional
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
	@Transactional
    public Boolean checkUserNoExists(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = findUserByNickname(principal.getUsername());
        if(user.isEmpty() || !user.get().isEnabled()) {
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
	 * @throws Exception
     */
	@Transactional
    public Boolean checkUser(HttpServletRequest request, User logedUser) throws NotExistLobbyException, ServletException {
		Boolean res;
		try {
		if(logedUser!=null && logedUser.isEnabled()) {
			Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
    
                List<User> users = lobby.getPlayerInternal();
                if (users.size() == 1) {
                    lobby.setActive(false);
                }
                users.remove(logedUser);
                lobby.setUsers(users);
                lobbyService.save(lobby);
            
            res = false;
        } else {
            request.getSession().invalidate();
            request.logout();
            res = true;
        }
		return res;
	   } catch (Exception e) {
		res = false;
		return res;
	   }
    }

	@Transactional
    public Boolean checkUser2(HttpServletRequest request, User logedUser) throws NotExistLobbyException, ServletException {
		Boolean res = false;
		try {
		if(logedUser!=null && !logedUser.isEnabled()) {
            request.getSession().invalidate();
            request.logout();
            res = true;
        }
		return res;
	   } catch (Exception e) {
		res = false;
		return res;
	   }
    }


	@Transactional
	public User addUser(User user, Boolean isAdmin, AuthenticationManager authenticationManager, HttpServletRequest request){
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
			
			if(!isAdmin) loginUser(user, password, request);
			return save(user);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public User updateUser(User newUserData, String param, Integer op){
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
			return save(oldUser);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
     * Realiza el logeo automático del usuario actual.
     * @param user
     * @param password
     */
	@Transactional
    public void loginUser(User user, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getNickname(), password);
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		authToken.setDetails(new WebAuthenticationDetails(request));
    }
}
