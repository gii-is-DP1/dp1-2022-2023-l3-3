package org.springframework.samples.sevenislands.game;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {
    @Query("SELECT game FROM Game game WHERE game.lobby.id=?1")
    public Game findGamebByLobbyId(Integer id);

    @Modifying
    @Query("UPDATE Game game SET game=?1 WHERE game.id=?2")
    public void updateGame(Game game, Integer game_id);

}
