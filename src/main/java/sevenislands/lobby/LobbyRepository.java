package sevenislands.lobby;

import java.util.Collection;
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
    
    //No se usa en ningún lado
    @Query(value = "SELECT lobby.id FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1")
    public Integer findLobbyIdByPlayer(Integer player_id);

    //No se usa en ningún lado
    @Query(value = "SELECT lobby.id FROM Lobby lobby INNER JOIN lobby.users user WHERE user.nickname=?1 AND lobby.active=true")
    public Optional<Integer> findLobbyByNicknamePlayer(String nickname);
    
    //No se usa en ningún lado
    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active=true")
    public List<Lobby> findAllActiveLobby();

    @Query("SELECT lobby FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1")
    public Optional<Lobby> findByPlayerId(Integer user_id);
}
