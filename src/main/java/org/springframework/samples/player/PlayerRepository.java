package org.springframework.samples.player;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends Repository<Player, Integer>{
    
    void save(Player player) throws DataAccessException;

    @Query("SELECT DISTINCT player FROM Player player WHERE player.name LIKE :name%")
	public Collection<Player> findByName(@Param("name") String name);

    @Query("SELECT player FROM Player player WHERE player.id = :id")
	public Player findById(@Param("id") Integer id);

    @Query("SELECT DISTINCT player FROM Player player WHERE player.email = :email")
	public Collection<Player> findEmail(@Param("email") String email);
}
