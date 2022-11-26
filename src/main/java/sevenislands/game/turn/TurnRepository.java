package sevenislands.game.turn;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnRepository extends CrudRepository<Turn, Integer> {

    @Query("SELECT turn FROM Turn turn WHERE turn.round.id=?1")
    public List<Turn> findByRoundId(int Id) throws DataAccessException;

    public List<Turn> findAll();

    //SELECT * FROM TURN T JOIN USER U ON U.ID=T.USER_ID WHERE U.NICKNAME='player2' ORDER BY T.ROUND_ID DESC LIMIT 1
    @Query("SELECT t FROM Turn t INNER JOIN t.user u WHERE u.nickname=?1 ORDER BY t.round.id DESC")
    public List<Turn> findTurnByNickname(String nickname);
    /*Posteriormente lo que habría que hacer para obtener el último turno sería hacer un .setMaxResults(1);
    ya que jquery no soporta el LIMIT*/
}
