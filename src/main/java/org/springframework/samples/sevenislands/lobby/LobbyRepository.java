package org.springframework.samples.sevenislands.lobby;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface LobbyRepository extends Repository<Lobby, Integer>{
    
    void save(Lobby lobby) throws DataAccessException;


    @Query("Select lobby from Lobby lobby where lobby.id = :id")
    public Lobby findById(@Param("id") Integer id);

    @Query("Select distinc lobby from Lobby lobby where lobby.active = :active")
    public Lobby findByActive(@Param("active") Integer active);
}
