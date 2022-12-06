package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.UserServiceTest;


@DataJpaTest
public class LobbyServiceTest {
    
    @Mock  
    LobbyRepository lobbyRepository;

    UserService userService;

    List<Lobby> listaLobby = new ArrayList<>();

    @BeforeEach
    public void config(){
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        userService = new UserService(null, lobbyService, null, null, null);
        User user1 = userService.createUser(1, "user1", "user1@gmail.com");
        User user2 = userService.createUser(2, "user2", "user2@gmail.com");
        User user3 = userService.createUser(3, "user3", "user3@gmail.com");
        User user4 = userService.createUser(4, "user4", "user4@gmail.com");
        Lobby lobby = new Lobby();
        Lobby lobby2 = new Lobby();
        lobby.addPlayer(user1);
        lobby.addPlayer(user2);
        lobby2.addPlayer(user3);
        lobby2.addPlayer(user4);
        listaLobby.add(lobby);
        listaLobby.add(lobby2);
       
        when(lobbyRepository.findAll()).thenReturn(listaLobby);
        
    }

    @Test
    public void numPartidasTest(){
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        Integer numberOfLobbys = lobbyService.findAll().size();
        assertEquals(2, numberOfLobbys);
    }

    @Test
    public void findLobbyByPlayerIdAndLobbyId() throws NotExistLobbyException {
        when(lobbyRepository.findByPlayerId(1)).thenReturn(Optional.of(listaLobby.get(0)));
        LobbyService lobbyService = new LobbyService(lobbyRepository);
        assertEquals(listaLobby.get(0).getCode(), lobbyService.findLobbyByPlayerId(1).getCode());
        assertThrows(NotExistLobbyException.class,() ->lobbyService.findLobbyByCode("fgcvcjcljhdbla.kv"));
    }
}
