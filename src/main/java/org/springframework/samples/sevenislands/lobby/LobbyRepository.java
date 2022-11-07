package org.springframework.samples.sevenislands.lobby;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby,Long> {

    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active = true")
    public Collection<Lobby> findAllActiveLobby();

    @Query("SELECT DISTINCT lobby FROM Lobby lobby where lobby.active = true AND lobby.code = :code")
    public Optional<Lobby> findByCode(@Param("code") Integer code);
    
}
