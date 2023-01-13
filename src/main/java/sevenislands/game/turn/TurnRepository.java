package sevenislands.game.turn;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.game.Game;
import sevenislands.game.round.Round;
import sevenislands.user.User;

@Repository
public interface TurnRepository extends CrudRepository<Turn, Integer> {

    @Query("SELECT turn FROM Turn turn WHERE turn.round=?1")
    public List<Turn> findByRound(Round round) throws DataAccessException;

    public List<Turn> findAll();

    //Para obtener el último turno tendriamos que coger el primer elemento de la lista
    @Query("SELECT t FROM Turn t INNER JOIN t.user user INNER JOIN t.round r INNER JOIN r.game g WHERE user=?1 ORDER BY t.round.id DESC")
    public Optional<List<Turn>> findTurnByUser(User user);

    @Query("SELECT COUNT(t) FROM Turn t INNER JOIN t.user u WHERE u=?1")
    public Integer findTotalTurnsByUser(User user);

    @Query("SELECT COUNT(t) FROM Turn t INNER JOIN t.round r INNER JOIN r.game g GROUP BY g")
    public List<Integer> findTotalTurnsByGame();

    @Query("SELECT COUNT(t) FROM Turn t GROUP BY TO_CHAR(t.startTime, 'YYYY-MM-DD')")
    public List<Integer> findTotalTurnsByDay();
    
    @Query("SELECT turn FROM Turn turn INNER JOIN turn.round round INNER JOIN round.game game WHERE game=?1")
    public List<Turn> findAllByGame(Game game);
}
