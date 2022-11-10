package org.springframework.samples.sevenislands.game.turn;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TurnRepository extends CrudRepository<Turn, Integer> {
    @Modifying
    @Query("UPDATE Turn turn SET turn=?1 WHERE turn.id=?2")
    public void updatePlayers(Turn turn, Integer turn_id);

    @Query("SELECT turn FROM Turn turn WHERE turn.round.id=:?1")
    List<Turn> findByRoundId(int Id) throws DataAccessException;
}
