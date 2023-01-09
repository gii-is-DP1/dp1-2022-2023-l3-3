package sevenislands.lobby.lobbyUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;

@Service
public class LobbyUserService {
    
    private final LobbyUserRepository lobbyUserRepository;
    private final LobbyService lobbyService;

    @Autowired
    public LobbyUserService(LobbyUserRepository lobbyUserRepository, LobbyService lobbyService) {
        this.lobbyUserRepository = lobbyUserRepository;
        this.lobbyService = lobbyService;
    }

    @Transactional
    public List<User> findUsersByLobby(Lobby lobby) {
        return lobbyUserRepository.findUsersByLobbyId(lobby);
    }

    @Transactional
    public LobbyUser findByLobbyAndUser(Lobby lobby, User user) {
        return lobbyUserRepository.findByLobbyAndUser(lobby, user);
    }

    @Transactional
    public void deleteLobbyUser(LobbyUser lobbyUser) {
        lobbyUserRepository.delete(lobbyUser);
    }

    @Transactional
    public Lobby findLobbyByUser(User user) throws NotExistLobbyException {
        Optional<List<LobbyUser>> lobbyList = lobbyUserRepository.findByUser(user);
        if(lobbyList.isPresent()) {
            Lobby lobby = lobbyList.get().get(0).getLobby();
            return lobby;
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional
    public void joinLobby(String code, User user) throws NotExistLobbyException {
        code = code.trim();
        Lobby lobby = lobbyService.findLobbyByCode(code);
        save(user, lobby, Mode.PLAYER);
    }

    @Transactional
    public LobbyUser save(User user, Lobby lobby, Mode mode) {
        LobbyUser lobbyUser = new LobbyUser();
        lobbyUser.setLobby(lobby);
        lobbyUser.setUser(user);
        lobbyUser.setMode(mode);
        return lobbyUserRepository.save(lobbyUser);
    }

    @Transactional
    public List<Lobby> findLobbiesByUser(User user) {
        Optional<List<LobbyUser>> lobbyList = lobbyUserRepository.findByUser(user);
        if(lobbyList.isPresent()) {
            return lobbyList.get().stream().map(LobbyUser::getLobby).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Integer findTotalPlayers() {
        return lobbyUserRepository.findTotalUsers();
    }

    @Transactional
    public Integer findTotalPlayersDistinct() {
        return lobbyUserRepository.findTotalUsersDistinct();
    }

    @Transactional
    public Integer findTotalPlayersByDay(List<Lobby> lobbyList) {
        return lobbyUserRepository.findTotalPlayersByDay(lobbyList);
    }

    @Transactional
    public List<Integer> findTotalPlayersByLobby(List<Lobby> lobbyList) {
        return lobbyUserRepository.findTotalPlayersByLobby(lobbyList);
    }

        /**
     * Compruena si el usuario se encuentra en una lobby.
     * @param request (Importar HttpServletRequest request en la función)
     * @return true (en caso de que no esté en una lobby) o false (en otro caso)
     * @throws ServletException
     */
    @Transactional
    public Boolean checkUserLobby(User logedUser) {
        Optional<Lobby> lobby = lobbyUserRepository.findLobbyByUserAndActive(logedUser, true);
        if(lobby.isPresent()) return true;
        return false;
    }

    @Transactional
    public void createLobby(User user) {
        Lobby lobby = lobbyService.createLobbyEntity();
        save(user, lobby, Mode.PLAYER);
    }
    
}
