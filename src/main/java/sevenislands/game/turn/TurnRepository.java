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

    //No se usa en ningún lado
    public List<Turn> findAll();

    //Para obtener el último turno tendriamos que coger el primer elemento de la lista
    @Query("SELECT t FROM Turn t INNER JOIN t.user u INNER JOIN t.round r INNER JOIN r.game g WHERE u.nickname=?1 AND g.active=true ORDER BY t.round.id DESC")
    public List<Turn> findTurnByNickname(String nickname);
    
}
