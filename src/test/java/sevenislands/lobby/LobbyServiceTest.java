package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

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
        Integer numberOfLobbys = lobbyService.findAll().size();
        assertNotEquals(0, numberOfLobbys);
    }

    @Test
    public void updateFindByIdsLobbyTest() throws NotExistLobbyException {
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        Lobby lobby = lobbyService.findLobbyByPlayerId(8).get();
        lobby.setCode("fgcvcjcljhdbla.kv");
        lobbyService.save(lobby);
        assertNotEquals(lobby.getCode(), lobbyService.findLobbyByPlayerId(8).get());
        Optional<Lobby> lobby2 = lobbyService.findLobbyByCode("fgcvcjcljhdbla.kv");
        assertNotNull(lobby2);
    }
}
