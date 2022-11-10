package org.springframework.samples.sevenislands.game.turn;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TurnRepository extends CrudRepository<Turn,Integer>{
    @Modifying
    @Query("UPDATE Turn turn SET turn=?1 WHERE turn.id=?2")
    public void updatePlayers(Turn turn, Integer turn_id);
}
