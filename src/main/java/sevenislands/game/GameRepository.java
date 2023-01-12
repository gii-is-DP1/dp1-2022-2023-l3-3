package sevenislands.game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.lobby.Lobby;


@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    public List<Game> findAll();
    @Query("SELECT game FROM Game game WHERE game.lobby.id=?1")
    public Optional<Game> findGameByLobbyId(Integer id);

    @Query("SELECT game FROM Game game WHERE game.lobby=?1")
    public Optional<Game> findGameByLobby(Lobby lobby);

    @Query("SELECT game FROM Game game WHERE game.lobby IN ?1")
    public Optional<List<Game>> findGamesByLobbies(List<Lobby> lobbies);

    @Query("SELECT winner, COUNT(game) FROM Game game INNER JOIN game.winner winner GROUP BY winner ORDER BY COUNT(game) DESC")
    List<Object []> findVictories();

    @Query("SELECT game FROM Game game INNER JOIN game.lobby lobby WHERE lobby IN ?1 AND game.active=?2 ORDER BY game.id DESC")
    public Optional<List<Game>> findGameByLobbyAndActive(List<Lobby> lobbies, Boolean active);

    @Query("SELECT COUNT(game) FROM Game game INNER JOIN game.winner winner WHERE winner.nickname=?1")
    Long findVictoriesByNickname(String nickname);

    @Query("SELECT game FROM Game game INNER JOIN game.winner winner WHERE winner.nickname=?1")
    List<Game> findWonGamesByNickname(String nickname);
    
    

    @Query("SELECT COUNT(game) FROM Game game INNER JOIN game.winner winner WHERE winner.nickname=?1 AND game.tieBreak=TRUE")
    public Long findTieBreaksByNickname(String nickname);

    @Query("SELECT COUNT(game) FROM Game game GROUP BY TO_CHAR(game.creationDate, 'YYYY-MM-DD')")
    public List<Integer> findTotalGamesPlayedByDay();

    @Query("SELECT COUNT(g) FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 GROUP BY TO_CHAR(g.creationDate, 'YYYY-MM-DD')")
    public List<Integer> findNGamesPlayedByNicknamePerDay(String nickname);

    @Query("SELECT g FROM Game g INNER JOIN g.lobby l INNER JOIN l.users u WHERE u.nickname=?1 GROUP BY TO_CHAR(g.creationDate, 'YYYY-MM-DD')")
    public List<Game> findGamesPlayedByNicknamePerDay(String nickname);
    @Query("SELECT COUNT(game) FROM Game game INNER JOIN game.lobby lobby WHERE lobby IN ?1")
    public Integer findTotalGamesPlayedByUserLobbies(List<Lobby> lobbies);

    @Query("SELECT lobby FROM Game game INNER JOIN game.lobby lobby GROUP BY TO_CHAR(game.creationDate, 'YYYY-MM-DD')")
    public List<List<Lobby>> findLobbiesByDay();

    @Query("SELECT lobby FROM Game game INNER JOIN game.lobby lobby")
    public List<Lobby> findLobbies();

    @Query("SELECT game FROM Game game WHERE game.active=?1")
    public List<Game> findGameActive(Boolean active);
}
