package org.springframework.samples.sevenislands.player;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.sevenislands.player.PlayerRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	@Query("SELECT player FROM Player player WHERE player.nickname=?1")
	public Player findByName(String name);

	@Modifying
	@Query("UPDATE Player player SET player=?1 WHERE player.id=?2")
	public void updatePlayer(Player player, Integer player_id);
}
