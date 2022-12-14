package sevenislands.game.island;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.card.Card;

@Repository
public interface IslandRepository extends CrudRepository<Island, Integer> {

<<<<<<< HEAD
    public List<Island> findAll();
=======
     public List<Island> findAll();
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722

    @Query("SELECT island.num FROM Island island WHERE island.id=?1")
    public Integer getIslandNumberById(int id) throws DataAccessException;

    @Query("SELECT island FROM Island island WHERE island.game.id=?1")
    public List<Island> findByGameId(Integer gameId);

    @Query("SELECT island FROM Island island WHERE island.card.id=?1")
    public Island findByCardId(Integer cardId);
<<<<<<< HEAD
=======

    @Query("SELECT island FROM Island island WHERE island.game.id=?1 AND island.num=?2")
    public Island findCardOfIsland(Integer gameId, Integer numIsland);
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
}
