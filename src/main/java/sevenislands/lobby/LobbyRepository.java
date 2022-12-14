package sevenislands.lobby;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby,Integer> {

    public List<Lobby> findAll();

    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.code=?1")
	public Optional<Lobby> findByCode(String code);
    
<<<<<<< HEAD
    //No se usa en ningún lado
    @Query(value = "SELECT lobby.id FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1 AND lobby.active=true")
    public Integer findLobbyIdByPlayer(Integer player_id);

    //No se usa en ningún lado
    @Query(value = "SELECT lobby.id FROM Lobby lobby INNER JOIN lobby.users user WHERE user.nickname=?1 AND lobby.active=true")
    public Optional<Integer> findLobbyByNicknamePlayer(String nickname);
    
    //No se usa en ningún lado
    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active=true")
    public List<Lobby> findAllActiveLobby();

    @Query("SELECT lobby FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1")
    public Optional<Lobby> findByPlayerId(Integer user_id);
=======
    @Query("SELECT lobby FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1")
    public Optional<Lobby> findByPlayerId(Integer user_id);

    @Query("SELECT lobby FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?2 AND lobby.active=?1")
    public List<Lobby> findLobbyActive(Boolean active,Integer user_id);


>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
}
