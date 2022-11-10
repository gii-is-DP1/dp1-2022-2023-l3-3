package org.springframework.samples.sevenislands.game.island;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.sevenislands.card.Card;

public interface IslandRepository extends CrudRepository<Island, Integer> {

    @Query("SELECT island.num FROM Island island WHERE island.id=:?1")
    Integer getIslandNumberById(int id) throws DataAccessException;

    @Query("SELECT island.cards FROM Island island WHERE island.id=:?1")
    List<Card> getIslandCardsById(int id) throws DataAccessException;
}
