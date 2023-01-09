package sevenislands.lobby.lobbyUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.lobby.Lobby;
import sevenislands.user.User;

@Repository
public interface LobbyUserRepository extends CrudRepository<LobbyUser, Integer>{
    
    @Query("SELECT lobbyUser.user FROM LobbyUser lobbyUser WHERE lobbyUser.lobby = ?1")
    public List<User> findUsersByLobbyId(Lobby lobby);

    @Query("SELECT lobbyUser FROM LobbyUser lobbyUser WHERE lobbyUser.lobby = ?1 AND lobbyUser.user = ?2")
    public LobbyUser findByLobbyAndUser(Lobby lobby, User user);

    @Query("SELECT lobbyUser FROM LobbyUser lobbyUser WHERE lobbyUser.user = ?1 ORDER BY lobbyUser.id DESC")
    public Optional<List<LobbyUser>> findByUser(User user);

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser")
    public Integer findTotalUsers();

    @Query("SELECT COUNT(DISTINCT user) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.user user")
    public Integer findTotalUsersDistinct();

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby WHERE lobby IN ?1")
    public Integer findTotalPlayersByDay(List<Lobby> lobbies);

    @Query("SELECT COUNT(lobbyUser) FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby WHERE lobby in ?1 GROUP BY lobby")
    public List<Integer> findTotalPlayersByLobby(List<Lobby> lobbies);

    @Query("SELECT lobby FROM LobbyUser lobbyUser INNER JOIN lobbyUser.lobby lobby INNER JOIN lobbyUser.user user WHERE user=?1 AND lobby.active=?2")
    public Optional<Lobby> findLobbyByUserAndActive(User user, Boolean active);
}
