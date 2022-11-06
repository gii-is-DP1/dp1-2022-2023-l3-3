package org.springframework.samples.sevenislands.lobby;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby,Long> {

    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active = 1")
    public Collection<Lobby> findAllActiveLobby();
    
}
