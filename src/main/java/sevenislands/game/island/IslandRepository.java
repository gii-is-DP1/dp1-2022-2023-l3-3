package sevenislands.game.island;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslandRepository extends CrudRepository<Island, Integer> {

    public List<Island> findAll();

    @Query("SELECT island.num FROM Island island WHERE island.id=?1")
    public Integer getIslandNumberById(int id) throws DataAccessException;

    @Query("SELECT island FROM Island island WHERE island.game.id=?1")
    public List<Island> findByGameId(Integer gameId);

    @Query("SELECT island FROM Island island WHERE island.card.id=?1")
    public Island findByCardId(Integer cardId);
}
