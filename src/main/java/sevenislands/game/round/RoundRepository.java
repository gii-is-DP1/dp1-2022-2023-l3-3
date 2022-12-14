package sevenislands.game.round;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoundRepository extends CrudRepository<Round, Integer> {

    public List<Round> findAll();
    
    @Query("SELECT round FROM Round round WHERE round.game.id=?1")
    public List<Round> findRoundByGameId(Integer id) throws DataAccessException;
}
