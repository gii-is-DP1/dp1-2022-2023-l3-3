package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LobbyRepositoryTest {
    
    @Autowired
    LobbyRepository lobbyRepository;

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Lobby> lobby= lobbyRepository.findAll();

        assertNotNull(lobby);
        assertFalse(lobby.isEmpty());
        assertNotEquals(0, lobby.size());
    }

    @Test
    public void TestUpdateLobby(){
        List<Lobby> lobbies= new ArrayList<>();
        lobbyRepository.findAll().iterator().forEachRemaining(lobbies::add);
        Lobby lobby=lobbies.get(0);
        lobby.setActive(false);
        lobby.setCode("123465445");
        lobbyRepository.save(lobby);
        assertNotNull(lobby);
        assertTrue(lobby.getCode().equals("123465445"));
    }



    @Test
    public void TestFindByCode(){
        Optional<Lobby> lobby=lobbyRepository.findByCode("aD5f8Lio");
        assertNotNull(lobby);
    }
    
    @Test
    public void TestGetNumOfLobby(){
        Long numOfLobby= StreamSupport.stream(lobbyRepository.findAll().spliterator(), false).count();
        assertTrue(numOfLobby>0);
    }

}
