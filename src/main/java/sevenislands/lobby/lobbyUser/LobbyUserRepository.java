package sevenislands.lobby.lobbyUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.enums.Mode;
import sevenislands.lobby.Lobby;
import sevenislands.user.User;

@Repository
public interface LobbyUserRepository extends CrudRepository<LobbyUser, Integer>{
    
    @Query("SELECT user FROM LobbyUser lobbyUser INNER JOIN lobbyUser.user user WHERE lobbyUser.lobby = ?1")
    public List<User> findUsersByLobby(Lobby lobby);

    @Query("SELECT lobbyUser FROM LobbyUser lobbyUser WHERE lobbyUser.lobby = ?1 AND lobbyUser.user = ?2")
    public Optional<LobbyUser> findByLobbyAndUser(Lobby lobby, User user);

    @Query("SELECT lobbyUser FROM LobbyUser lobbyUser WHERE lobbyUser.user.nickname = ?1 AND lobbyUser.mode=?2 ORDER BY lobbyUser.id DESC")
    public Optional<List<LobbyUser>> findByUserAndMode(String nickname, Mode mode);

    @Query("SELECT lobbyUser FROM LobbyUser lobbyUser WHERE lobbyUser.user = ?1 ORDER BY lobbyUser.id DESC")
    public Optional<List<LobbyUser>> findByUser(User user);

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser WHERE lobbyUser.mode=?1")
    public Integer findTotalUsersByMode(Mode mode);

    @Query("SELECT COUNT(DISTINCT user) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.user user WHERE lobbyUser.mode=?1")
    public Integer findTotalUsersDistinct(Mode mode);

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby WHERE lobby IN ?1 AND lobbyUser.mode=?2")
    public Integer findTotalPlayersByDayAndMode(List<Lobby> lobbies, Mode mode);

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby WHERE lobby IN ?1 AND lobbyUser.mode=?2 GROUP BY lobby")
    public Optional<List<Long>> findTotalPlayersByLobbyAndMode(List<Lobby> lobbies, Mode mode);

    @Query("SELECT lobby FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby INNER JOIN lobbyUser.user user WHERE user=?1 AND lobby.active=?2")
    public Optional<Lobby> findLobbyByUserAndActive(User user, Boolean active);

    @Query("SELECT lobbyUser.user FROM LobbyUser lobbyUser WHERE lobbyUser.lobby = ?1 AND lobbyUser.mode = ?2")
    public List<User> findUsersByLobbyAndMode(Lobby lobby, Mode mode);
}
