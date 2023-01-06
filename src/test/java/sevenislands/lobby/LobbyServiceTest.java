package sevenislands.lobby;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameRepository;
import sevenislands.game.GameService;
import sevenislands.user.User;
import sevenislands.user.UserService;


@DataJpaTest
public class LobbyServiceTest {
    
    @Mock  
    LobbyRepository lobbyRepository;
    @Mock
    GameRepository gameRepository;

    UserService userService;
    GameService gameService;

    List<Lobby> listaLobby = new ArrayList<>();

    @BeforeEach
    public void config(){
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        userService = new UserService(null, lobbyService, null, null, null, null);
        User user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        User user2 = userService.createUser(2, "user2Test", "user2Test@gmail.com");
        User user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        User user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        Lobby lobby = new Lobby();
        Lobby lobby2 = new Lobby();
        lobby.addPlayer(user1);
        lobby.addPlayer(user2);
        lobby2.addPlayer(user3);
        lobby2.addPlayer(user4);
        listaLobby.add(lobby);
        listaLobby.add(lobby2);

        gameService = new GameService(null, gameRepository);
       
        when(lobbyRepository.findAll()).thenReturn(listaLobby);
        
    }

    @Test
    public void numPartidasTest(){
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        Integer numberOfLobbys = lobbyService.findAll().size();
        assertEquals(2, numberOfLobbys);
    }

    @Test
    public void findLobbyByPlayerIdAndLobbyIdTest() throws NotExistLobbyException {
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        when(lobbyRepository.findByPlayerId(1)).thenReturn(Optional.of(listaLobby));
        assertThrows(NotExistLobbyException.class,() ->lobbyService.findLobbyByCode("fgcvcjcljhdbla.kv"));
        when(lobbyRepository.findByCode(any())).thenReturn(Optional.of(listaLobby.get(0)));
        assertEquals(listaLobby.get(0).getCode(), lobbyService.findLobbyByPlayerId(1).getCode());
        assertEquals(listaLobby.get(0).getId(), lobbyService.findLobbyByCode(this.listaLobby.get(0).getCode()).getId());
    }

    @Test
    public void validateJoinTest() throws NotExistLobbyException {
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        when(lobbyRepository.findByCode(any())).thenReturn(Optional.of(listaLobby.get(0)));
        assertFalse(lobbyService.checkLobbyErrors("nbtybyrnt").isEmpty());
        listaLobby.get(0).setActive(true);
        assertTrue(lobbyService.checkLobbyErrors("nbtybyrnt").isEmpty());

    }

    @Test
    public void leaveLobbyTest() throws NotExistLobbyException {
        User user = listaLobby.get(0).getUsers().get(0);
        LobbyService lobbyService = new LobbyService(lobbyRepository, gameService);
        when(lobbyRepository.findByPlayerId(any())).thenReturn(Optional.of(listaLobby));
        when(lobbyRepository.save(any())).thenReturn(listaLobby.get(0));
        assertEquals(lobbyService.leaveLobby(user).getUsers().size(), this.listaLobby.get(0).getUsers().size());
        assertFalse(lobbyService.leaveLobby(user).isActive());
    }

    @Test
    public void createLobbyTest(){
        User userPrueba = userService.createUser(10, "userTest", "userTest@gmail.com");
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        Lobby lobbyPrueba = lobbyService.createLobbyEntity(userPrueba);
        when(lobbyService.save(any())).thenReturn(lobbyPrueba);
        assertTrue(lobbyPrueba.getUsers().contains(userPrueba));
        Lobby lobbyPrueba2 = lobbyService.createLobby(userPrueba);
        assertTrue(lobbyPrueba2.getUsers().contains(userPrueba));
    }

    @Test
    public void disableLobbyTest(){
        User userPrueba = userService.createUser(10, "userTest", "userTest@gmail.com");
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        Lobby lobbyPrueba = lobbyService.createLobbyEntity(userPrueba);
        when(lobbyService.save(any())).thenReturn(lobbyPrueba);
        assertFalse(lobbyService.disableLobby(lobbyPrueba).isActive());
    }

    @Test
    public void noLobbyUser(){
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        User userPrueba2 = listaLobby.get(0).getUsers().get(0);
        when(lobbyRepository.findByPlayerId(userPrueba2.getId())).thenReturn(Optional.of(listaLobby.stream().filter(l -> l.getUsers().contains(userPrueba2)).collect(Collectors.toList())));
        assertFalse(lobbyService.checkUserLobby(userPrueba2));
    }

    @Test
    public void lobbyErrorsTest() throws NotExistLobbyException{
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        String code = listaLobby.get(0).getCode();
        when(lobbyRepository.findByCode(code)).thenReturn(Optional.of(listaLobby.get(0)));
        assertEquals(1, lobbyService.checkLobbyErrors(code).size());
        User user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        User user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        listaLobby.get(0).addPlayer(user4);
        listaLobby.get(0).addPlayer(user3);
        assertEquals(1, lobbyService.checkLobbyErrors(code).size());
        listaLobby.get(0).setActive(true);
        assertEquals(1, lobbyService.checkLobbyErrors(code).size());
    }

    @Test
    public void checkLobbyNoAllPlayersTest() throws Exception{
        User user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        User user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        User user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        User user5 = userService.createUser(5, "user5Test", "user5Test@gmail.com");
        listaLobby.get(0).setActive(true);
        LobbyService lobbyService = new LobbyService(lobbyRepository, null);
        Integer code = listaLobby.get(0).getUsers().get(0).getId();
        when(lobbyRepository.findByPlayerId(code)).thenReturn(Optional.of(listaLobby));
        assertThrows(NotExistLobbyException.class, ()-> lobbyService.checkLobbyNoAllPlayers(user3));
        assertFalse(lobbyService.checkLobbyNoAllPlayers(user1));
        listaLobby.get(0).addPlayer(user4);
        listaLobby.get(0).addPlayer(user3);
        listaLobby.get(0).addPlayer(user5);
        assertTrue(lobbyService.checkLobbyNoAllPlayers(user1));
    }

    @Test
    public void ejectPlayersTest() throws Exception{
        User user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        User user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        User user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        listaLobby.get(0).setActive(true);
        LobbyService lobbyService = new LobbyService(lobbyRepository, gameService);
        Integer code = listaLobby.get(0).getUsers().get(0).getId();
        Integer code2 = listaLobby.get(1).getUsers().get(0).getId();
        when(lobbyRepository.findByPlayerId(code)).thenReturn(Optional.of(listaLobby));
        assertThrows(NotExistLobbyException.class, ()-> lobbyService.ejectPlayer(user1,user3));
        when(lobbyRepository.findByPlayerId(code2)).thenReturn(Optional.of(listaLobby));
        assertTrue(lobbyService.ejectPlayer(user1, user3));
        assertFalse(lobbyService.ejectPlayer(user3, user3));
        when(lobbyRepository.findByPlayerId(any())).thenReturn(Optional.of(listaLobby));
        assertTrue(lobbyService.ejectPlayer(user3, user4));

    }
}
