package org.springframework.samples.sevenislands.player;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.player.PlayerRepository;

public interface PlayerRepository extends Repository<Player, Integer> {
	
	void save(Player player) throws DataAccessException;

	@Query("SELECT player FROM Player player WHERE player.nickname=?1")
	public Player findByName(String name);
	
	@Query("SELECT player FROM Player player WHERE player.id=?1")
	public Player findById(Integer id);

	@Modifying
    @Query("UPDATE Player player SET player=?1 WHERE player.id=?2")
    public void updatePlayer(Player player, Integer player_id);
}
