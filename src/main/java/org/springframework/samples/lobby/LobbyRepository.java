package org.springframework.samples.lobby;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface LobbyRepository extends Repository<Lobby, Integer>{
    
    void save(Lobby lobby) throws DataAccessException;


    @Query("Select lobby from Lobby lobby where lobby.id = :id")
    public Lobby findById(@Param("id") Integer id);
}
