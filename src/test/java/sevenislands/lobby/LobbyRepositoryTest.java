package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public void TestUpdateLobby(){
        List<Lobby> lobbies= new ArrayList<>();
        lobbyRepository.findAll().iterator().forEachRemaining(lobbies::add);
        Lobby lobby=lobbies.get(0);
        lobby.setActive(false);
        lobby.setCode("123465445");
        lobbyRepository.updateLobby(lobby, lobby.getId());
        assertNotNull(lobby);
        assertTrue(lobby.getCode().equals("123465445"));
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
    public void TestFindLobbyByNicknameSuccess(){
        Optional<Integer> lobby=lobbyRepository.findLobbyByNicknamePlayer("player1");
        assertNotNull(lobby);
    }  

    @Test
    public void TestFindLobbyByNicknameFail(){
        Optional<Integer> lobby=lobbyRepository.findLobbyByNicknamePlayer("player2");
        assertNull(lobby.orElse(null));
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
