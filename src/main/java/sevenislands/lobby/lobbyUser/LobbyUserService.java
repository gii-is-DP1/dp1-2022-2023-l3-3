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
        return lobbyUserRepository.findUsersByLobby(lobby);
    }

    @Transactional
    public Optional<LobbyUser> findByLobbyAndUser(Lobby lobby, User user) {
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
    public void joinLobby(String code, User user, Mode mode) throws NotExistLobbyException {
        code = code.trim();
        Lobby lobby = lobbyService.findLobbyByCode(code);
        save(user, lobby, mode);
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
    public List<Lobby> findLobbiesByUserAndMode(User user, Mode mode) {
        Optional<List<LobbyUser>> lobbyList = lobbyUserRepository.findByUserAndMode(user, mode);
        if(lobbyList.isPresent()) {
            return lobbyList.get().stream().map(LobbyUser::getLobby).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
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
    public Integer findTotalPlayersByMode(Mode mode) {
        return lobbyUserRepository.findTotalUsersByMode(mode);
    }

    @Transactional
    public Integer findTotalPlayersDistinctByMode(Mode mode) {
        return lobbyUserRepository.findTotalUsersDistinct(mode);
    }

    @Transactional
    public Integer findTotalPlayersByDayAndMode(List<Lobby> lobbyList, Mode mode) {
        return lobbyUserRepository.findTotalPlayersByDayAndMode(lobbyList, mode);
    }

    @Transactional
    public Optional<List<Integer>> findTotalPlayersByLobbyAndMode(List<Lobby> lobbyList, Mode mode) {
        return lobbyUserRepository.findTotalPlayersByLobbyAndMode(lobbyList, mode);
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

    @Transactional
    public List<User> findUsersByLobbyAndMode(Lobby lobby, Mode mode) {
        return lobbyUserRepository.findUsersByLobbyAndMode(lobby, mode);
    }
}
