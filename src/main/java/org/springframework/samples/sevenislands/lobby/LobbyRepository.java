package org.springframework.samples.sevenislands.lobby;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LobbyRepository extends CrudRepository<Lobby, Integer>{

    @Query("Select distinc lobby from Lobby lobby where lobby.active = :active")
    public Lobby findByActive(@Param("active") Integer active);
}
