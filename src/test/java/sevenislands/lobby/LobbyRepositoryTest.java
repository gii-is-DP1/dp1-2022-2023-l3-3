package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LobbyRepositoryTest {
    
    @Autowired
    LobbyRepository lobbyRepository;

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Lobby> lobby= new ArrayList<>();
        lobbyRepository.findAll().iterator().forEachRemaining(lobby::add);

        assertNotNull(lobby);
        assertFalse(lobby.isEmpty());
        assertEquals(1, lobby.size());
    }


    @Test
    public void TestFindByCode(){
        Lobby lobby=lobbyRepository.findByCode("aD5f8Lio");
        assertNotNull(lobby);
    }
    
    @Test
    public void TestFindLobbyIdByPlayer(){
        Integer lobby=lobbyRepository.findLobbyIdByPlayer(8);
        assertNotNull(lobby);
    }  

    @Test
    public void TestFindAllActiveLobby(){
        Collection<Lobby> lobby=lobbyRepository.findAllActiveLobby();
        assertTrue(!lobby.isEmpty());
    }

    @Test
    public void TestCheckLobby(){
        Boolean lobby=lobbyRepository.checkLobby("aD5f8Lio");
        assertTrue(lobby);
    } 

    @Test
    public void TestGetNumOfLobby(){
        Integer numOfLobby=lobbyRepository.getNumOfLobby();
        assertTrue(numOfLobby>0);
    }

}
