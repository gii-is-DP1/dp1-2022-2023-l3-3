package org.springframework.samples.sevenislands.lobby;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LobbyRepository extends CrudRepository<Lobby,Integer> {

    @Modifying
    @Query("UPDATE Lobby lobby SET lobby=?1 WHERE lobby.id=?2")
    public void updateLobby(Lobby lobby, Integer lobby_id);

    @Modifying
    @Query("UPDATE Lobby lobby SET lobby=?1 WHERE lobby.id=?2")
    public void updatePlayers(Lobby lobby, Integer lobby_id);
    
    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.code=?1")
	public Optional<Lobby> findByCode(String code);

    @Query(value = "SELECT l.lobby_id FROM lobby_players l WHERE l.players_id=?1", nativeQuery = true)
	public Integer findByPlayer(Integer player_id);
    
    @Query("SELECT lobby FROM Lobby lobby WHERE lobby.id=?1")
    public Lobby findByLobbyId(Integer code);

    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active = true")
    public Collection<Lobby> findAllActiveLobby();
}
