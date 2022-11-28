package sevenislands.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.user.User;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;
    private final Integer minPlayers = 1;
    private final Integer maxPlayers = 4;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository){
        this.lobbyRepository=lobbyRepository;
    }

    /**
     * Crea un código aleatorio para la lobby.
     * @return String
     */
    public String generatorCode() {
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Integer RANDOM_STRING_LENGTH = 8;
        StringBuffer randomString = new StringBuffer();
        
        for(int i = 0; i<RANDOM_STRING_LENGTH; i++) {
            Random randomGenerator = new Random();
            char ch = CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()-1));
            randomString.append(ch);
        }
        return randomString.toString();
    }

    @Transactional
	public void save(Lobby lobby) {
        lobbyRepository.save(lobby);
	}

    @Transactional
    public List<Lobby> findAll() {
        return StreamSupport.stream(lobbyRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Transactional(readOnly = true, rollbackFor = NotExistLobbyException.class)
    public Optional<Lobby> findLobbyByCode(String code) throws NotExistLobbyException {
        Optional<Lobby> lobby = lobbyRepository.findByCode(code);
        if(lobby != null){
            return lobby;
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional(rollbackFor = NotExistLobbyException.class)
    public Optional<Lobby> findLobbyByPlayerId(Integer user_id) {
        return lobbyRepository.findByPlayerId(user_id);
    }

    @Transactional
	public Boolean checkUserLobbyByNickname(String name) {
	    return lobbyRepository.findLobbyByNicknamePlayer(name)!=null;
	}

    @Transactional
    public void createLobby(User user) {
		Lobby lobby = new Lobby();
		lobby.setCode(generatorCode());
		lobby.setActive(true);
		lobby.addPlayer(user);
		save(lobby);
    }

    @Transactional
    public void leaveLobby(User user) {
		Optional<Lobby> lobby = findLobbyByPlayerId(user.getId());
		if(lobby.isPresent()) {
            Lobby lobby2 = lobby.get();
            List<User> users = lobby2.getPlayerInternal();
		if (users.size() == minPlayers) {
			lobby2.setActive(false);
		}
		users.remove(user);
		lobby2.setUsers(users);
		save(lobby2);
        }
    }

    @Transactional
    public Boolean ejectPlayer(User logedUser, User userEjected) {
		Lobby lobby = findLobbyByPlayerId(userEjected.getId()).get();
		List<User> users = lobby.getPlayerInternal();
		if (userEjected.getNickname().equals(logedUser.getNickname())) {
            leaveLobby(logedUser);
			return false;
		} else {
			users.remove(userEjected);
			lobby.setUsers(users);
			save(lobby);
			return true;
		}
    }

    @Transactional
    public Boolean validateJoin(String code, User user) throws NotExistLobbyException {
        code = code.trim();
        Optional<Lobby> lobby = findLobbyByCode(code);
        Integer userNumber = lobby.get().getUsers().size();
		if (lobby.isPresent() && lobby.get().isActive() && userNumber != null && userNumber >= minPlayers && userNumber < maxPlayers) {
			lobby.get().addPlayer(user);
			save(lobby.get());
            return true;
		} else return false;
    }

    @Transactional
    public List<String> checkLobbyErrors(String code) throws NotExistLobbyException {
        List<String> errors = new ArrayList<>();
		Optional<Lobby> lobby = findLobbyByCode(code);
        if(lobby.isPresent()) {
            Integer userNumber = lobby.get().getUsers().size();
		    if(!lobby.get().isActive()) errors.add("La partida ya ha empezado o ha finalizado");
		    if(userNumber == maxPlayers) errors.add("La lobby está llena");
        }
        errors.add("No existe ninguna partida con ese código");
        return errors;
    }

    /**
     * Compruena si el usuario se encuentra en una lobby.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (en caso de que no esté en una lobby) o false (en otro caso)
     * @throws ServletException
     */
    @Transactional
    public Boolean checkUserNoLobby(User loggedUser) {
        return !lobbyRepository.findByPlayerId(loggedUser.getId()).isPresent();
    }
}