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
    
    @Query("SELECT lobby FROM Lobby lobby INNER JOIN lobby.users user WHERE user.id=?1")
    public Optional<Lobby> findByPlayerId(Integer user_id);
}
