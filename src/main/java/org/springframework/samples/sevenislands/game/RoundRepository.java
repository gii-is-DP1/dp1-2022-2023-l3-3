package org.springframework.samples.sevenislands.game;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoundRepository extends CrudRepository<Round, Integer> {

    @Query("SELECT round FROM Round round WHERE round.game.id=:?1")
    List<Round> findRoundByGameId(int id) throws DataAccessException;
}
