package sevenislands.game;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    @Query("SELECT count(game) FROM Game game")
    public Integer getNumOfGames();

    @Query("SELECT game FROM Game game WHERE game.lobby.id=?1")
    public Optional<Game> findGamebByLobbyId(Integer code);
     
    //SELECT G.ID  FROM GAME G JOIN LOBBY L ON  G.LOBBY_ID = L.ID INNER JOIN USER U WHERE U.NICKNAME='player1'
     @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=true")
     public Optional<Game> findGameByNickname(String nickname);
}
