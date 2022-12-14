package sevenislands.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.user.User;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;
    private GameService gameService;

    private final Integer minPlayers = 1;
    private final Integer maxPlayers = 4;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository, GameService gameService) {
        this.lobbyRepository=lobbyRepository;
        this.gameService = gameService;
    }

    /**
     * Crea un código aleatorio para la lobby.
     * @return String
     */
    /*public String generatorCode() {
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Integer RANDOM_STRING_LENGTH = 8;
        StringBuffer randomString = new StringBuffer();
        
        for(int i = 0; i<RANDOM_STRING_LENGTH; i++) {
            Random randomGenerator = new Random();
            char ch = CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()));
            randomString.append(ch);
        }
        return randomString.toString();
    }*/

    @Transactional
	public Lobby save(Lobby lobby) {
        return lobbyRepository.save(lobby);
	}

    @Transactional
    public List<Lobby> findAll() {
        return lobbyRepository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = NotExistLobbyException.class)
    public Lobby findLobbyByCode(String code) throws NotExistLobbyException {
        Optional<Lobby> lobby = lobbyRepository.findByCode(code);
        if(lobby.isPresent() && lobby != null){
            return lobby.get();
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional
    public  Lobby findLobbyByPlayerId(Integer user_id) throws NotExistLobbyException {
        Optional<List<Lobby>> lobbyList = lobbyRepository.findByPlayerId(user_id);
        if(lobbyList.isPresent()){
            Lobby lobby = lobbyList.get().get(0);
            return lobby;
        } else {
            throw new NotExistLobbyException();
        }
    }


    public Lobby createLobbyEntity(User user) {
		Lobby lobby = new Lobby();
		lobby.generatorCode();
		lobby.setActive(true);
		lobby.addPlayer(user);
    return lobby;
    }

    @Transactional
    public Lobby createLobby(User user) {
		Lobby lobby = createLobbyEntity(user);
		return save(lobby);
    }

    @Transactional
    public Lobby leaveLobby(User user) throws NotExistLobbyException {
		
        Optional<Game> game = gameService.findGameByNickname(user.getNickname());
        Lobby lobby = findLobbyByPlayerId(user.getId());
		if(lobby.isActive() || (game.isPresent() && game.get().isActive())) {
            List<User> users = lobby.getPlayerInternal();
		if (users.size() == minPlayers) {
			lobby.setActive(false);
		}
		users.remove(user);
		lobby.setUsers(users);
		lobby = save(lobby);
        } 
        return lobby;
    }

    @Transactional
    public Boolean ejectPlayer(User logedUser, User ejectedUser) throws Exception {
		try {
            Lobby lobbyEjectedUser = findLobbyByPlayerId(ejectedUser.getId());
        Lobby lobbyLogedUser = findLobbyByPlayerId(logedUser.getId());
        Boolean res;
        if(lobbyEjectedUser.equals(lobbyLogedUser)) {
            List<User> users = lobbyEjectedUser.getPlayerInternal();
            if (ejectedUser.getNickname().equals(logedUser.getNickname())) {
                leaveLobby(logedUser);
                res = false;
            } else {
                users.remove(ejectedUser);
                lobbyEjectedUser.setUsers(users);
                save(lobbyEjectedUser);
                res = true;
            }
        } else{res= true;} return res;
        } catch (Exception e) {
            throw e;
        }
        
    }

    @Transactional
    public void joinLobby(String code, User user) throws NotExistLobbyException {
        code = code.trim();
        Lobby lobby = findLobbyByCode(code);
        lobby.addPlayer(user);
        save(lobby);
    }

    @Transactional
    public List<String> checkLobbyErrors(String code) throws NotExistLobbyException {
        List<String> errors = new ArrayList<>();
        try {
            code = code.trim();
            Lobby lobby = findLobbyByCode(code);
            Integer userNumber = lobby.getUsers().size();
		    if(!lobby.isActive()) errors.add("La partida ya ha empezado o ha finalizado");
		    if(userNumber == maxPlayers) errors.add("La lobby está llena");
            return errors;
        } catch (Exception e) {
            errors.add("El código no pertenece a ninguna lobby");
            return errors;
        }
        
       
    }

    /**
     * Compruena si el usuario se encuentra en una lobby.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (en caso de que no esté en una lobby) o false (en otro caso)
     * @throws ServletException
     */
    @Transactional
    public Boolean checkUserLobby(User logedUser) {
        Optional<List<Lobby>> lobby = lobbyRepository.findLobbyActive(true,logedUser.getId());
        if(lobby.isPresent()) return true;
        return false;
    }

    @Transactional
    public Boolean checkLobbyNoAllPlayers(User logedUser) throws Exception {
        try {
            Integer userNumber = null;
        Lobby lobby = findLobbyByPlayerId(logedUser.getId());
        Boolean res;
        userNumber = lobby.getUsers().size();
		if (userNumber != null && userNumber > minPlayers && userNumber <= maxPlayers) {
            res = false;
		} else {res= true;}
        return res;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Lobby disableLobby(Lobby lobby) {
        lobby.setActive(false);
        return lobbyRepository.save(lobby);
    }

    @Transactional
    public Double findAveragePlayersInGameById(Integer userId) {
        Optional<List<Lobby>> allUserLobbies = lobbyRepository.findByPlayerId(userId);
        Double averagePlayers = 0.;
        if(allUserLobbies.isPresent()) {
            Integer numGames = allUserLobbies.get().size();
            Integer totalUsers = allUserLobbies.get().stream()
            .map(lobby -> lobby.getUsers().size())
            .reduce(0, (a,b) -> a + b);
            averagePlayers = (double) totalUsers / numGames;
        }
        return averagePlayers;
    }

    @Transactional
    public Integer findMaxPlayersInGameById(Integer userId) {
        Optional<List<Lobby>> allUserLobbies = lobbyRepository.findByPlayerId(userId);
        Integer maxPlayers = 0;
        if(allUserLobbies.isPresent()) {
            maxPlayers = allUserLobbies.get().stream()
            .map(lobby -> lobby.getUsers().size())
            .reduce(0, Integer::max);
        }
        return maxPlayers;
    }

    @Transactional
    public Integer findMinPlayersInGameById(Integer userId) {
        Optional<List<Lobby>> allUserLobbies = lobbyRepository.findByPlayerId(userId);
        Integer minPlayers = 0;
        if(allUserLobbies.isPresent()) {
            minPlayers = allUserLobbies.get().stream()
            .map(lobby -> lobby.getUsers().size())
            .min(Integer::compareTo)
            .get();
        }
        return minPlayers;
    }
}