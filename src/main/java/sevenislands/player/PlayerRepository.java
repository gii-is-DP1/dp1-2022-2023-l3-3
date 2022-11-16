package sevenislands.player;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sevenislands.player.PlayerRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	
	public List<Player> findAll();

	@Query("SELECT player FROM Player player WHERE player.nickname=?1")
	public Optional<Player> findByName(String name);

	@Modifying
	@Query("UPDATE Player player SET player=?1 WHERE player.id=?2")
	public void updatePlayer(Player player, Integer player_id);
}
