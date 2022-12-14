package sevenislands.game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    public List<Game> findAll();
    @Query("SELECT game FROM Game game WHERE game.lobby.id=?1")
    public Optional<Game> findGamebByLobbyId(Integer code);
    



    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=?2")
    public Optional<Game> findGameByNickname(String nickname, Boolean active);

    @Query("SELECT g FROM Game g WHERE g.active=?1")
    public List<Game> findGamesActive(Boolean active);
}
