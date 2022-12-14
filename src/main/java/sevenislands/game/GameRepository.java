package sevenislands.game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    public List<Game> findAll();
<<<<<<< HEAD


=======
    
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
    @Query("SELECT game FROM Game game WHERE game.lobby.id=?1")
    public Optional<Game> findGamebByLobbyId(Integer code);
    

<<<<<<< HEAD
     
    //SELECT G.ID  FROM GAME G JOIN LOBBY L ON  G.LOBBY_ID = L.ID INNER JOIN USER U WHERE U.NICKNAME='player1'
    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=true")
    public Optional<Game> findGameByNickname(String nickname);
=======

    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=?2")
    public Optional<Game> findGameByNickname(String nickname, Boolean active);
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722

    @Query("SELECT g FROM Game g WHERE g.active=?1")
    public List<Game> findGamesActive(Boolean active);
}
