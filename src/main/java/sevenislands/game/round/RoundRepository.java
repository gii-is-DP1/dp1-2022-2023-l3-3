package sevenislands.game.round;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.game.Game;


@Repository
public interface RoundRepository extends CrudRepository<Round, Integer> {

    public List<Round> findAll();
    
    @Query("SELECT round FROM Round round WHERE round.game=?1")
    public List<Round> findRoundByGame(Game game) throws DataAccessException;
}
