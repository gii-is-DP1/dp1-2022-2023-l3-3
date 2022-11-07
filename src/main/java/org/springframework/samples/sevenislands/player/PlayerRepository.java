package org.springframework.samples.sevenislands.player;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.samples.sevenislands.model.BaseEntity;
import org.springframework.samples.sevenislands.player.PlayerRepository;

public interface PlayerRepository extends Repository<Player, Integer> {
	
	void save(Player player) throws DataAccessException;

	@Query("SELECT player FROM Player player WHERE player.nickname =?1")
	public Player findByName(String name);
}
