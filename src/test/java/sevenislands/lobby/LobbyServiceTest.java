package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.exceptions.NotExistLobbyException;


@DataJpaTest
public class LobbyServiceTest {
    
    @Autowired    
    LobbyRepository lobbyRepository;

    @Test
    public void numPartidasTest(){
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        Integer number = lobbyService.numPartidas();
        assertNotEquals(0, number);
    }

    @Test
    public void updateFindByIdsLobbyTest() throws NotExistLobbyException {
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        Lobby lobby = lobbyService.findLobbyByPlayer(8).get();
        lobby.setCode("fgcvcjcljhdbla.kv");
        lobbyService.update(lobby);
        assertNotEquals(lobby.getCode(), lobbyService.findLobbyByPlayer(8).get());
        Lobby lobby2 = lobbyService.findLobbyByCode("fgcvcjcljhdbla.kv");
        assertNotNull(lobby2);
    }
    
    @Test
    public void checksLobbyTest() throws NotExistLobbyException {
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        
        assertTrue(lobbyService.checkUserLobbyByName(8));
        assertTrue(lobbyService.checkLobbyByCode("aD5f8Lio"));
        assertFalse(lobbyService.checkLobbyByCode("aD5f8Lvbnio"));
    }
 
}
