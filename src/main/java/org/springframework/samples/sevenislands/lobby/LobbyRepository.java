package org.springframework.samples.sevenislands.lobby;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.sevenislands.player.Player;

public interface LobbyRepository extends CrudRepository<Lobby,Integer> {

    @Modifying
    @Query("UPDATE Lobby lobby SET lobby=?1 WHERE lobby.id=?2")
    public void updateLobby(Lobby lobby, Integer lobby_id);

    @Modifying
    @Query("UPDATE Lobby lobby SET lobby=?1 WHERE lobby.id=?2")
    public void updatePlayers(Lobby lobby, Integer lobby_id);
    
    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.code=?1")
	public Lobby findByCode(String code);
    
    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.id=?1")
	public Lobby findLobbyById(Integer lobby_id);

    @Query(value = "SELECT l.lobby_id FROM lobby_players l WHERE l.players_id=?1", nativeQuery = true)
	public Integer findByPlayer(Integer player_id);
    

    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.id=?1")
    public Lobby findByLobbyId(Integer code);
}
