package org.springframework.samples.sevenislands.game;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TurnRepository extends CrudRepository<Turn, Integer> {

    List<Turn> findAll() throws DataAccessException;

    @Query("SELECT turn FROM Turn turn WHERE turn.round.id=:?1")
    List<Turn> findByRoundId(int Id) throws DataAccessException;

}
