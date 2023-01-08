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

    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=?2 ORDER BY g.id DESC")
    public Optional<List<Game>> findGameByNicknameAndActive(String nickname, Boolean active);

    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 ORDER BY g.id DESC")
    public Optional<List<Game>> findGameByNickname(String nickname);

    @Query("SELECT g, u.nickname FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE g.active=?1")
    public List<Object []> findGameActive(Boolean active);

    @Query("SELECT g, u.nickname FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 AND g.active=false ORDER BY g.id DESC")
    public List<Object []> findGamePLayedByNickname(String nickname);

    @Query("SELECT COUNT(g) FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1")
    public Integer findTotalGamesPlayedByNickname(String nickname);

    @Query("SELECT COUNT(game) FROM Game game INNER JOIN game.winner winner WHERE winner.nickname=?1")
    Long findVictoriesByNickname(String nickname);
    
    @Query("SELECT winner, COUNT(game) FROM Game game INNER JOIN game.winner winner GROUP BY winner ORDER BY COUNT(game) DESC")
    List<Object []> findVictories();

    @Query("SELECT COUNT(game) FROM Game game INNER JOIN game.winner winner WHERE winner.nickname=?1 AND game.tieBreak=TRUE")
    Long findTieBreaksByNickname(String nickname);
    
    @Query("SELECT COUNT(g) FROM Game g GROUP BY TO_CHAR(g.creationDate, 'YYYY-MM-DD')")
    public List<Integer> findTotalGamesPlayedByDay();

}
