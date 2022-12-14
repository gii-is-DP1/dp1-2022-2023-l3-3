package sevenislands.lobby;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@DataJpaTest
public class LobbyRepositoryTest {
    
    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    UserRepository userRepository;

    UserService userService;

    Lobby lobbyTest;
    List<User> users;

    @BeforeEach
    public void config(){
        userService = new UserService(null, null, null, null, userRepository);
        IntStream.range(0, 3).forEach(i -> {
            userRepository.save(userService.createUser(10000+i, "playerTest"+i, "EmailTest"+i+"@gmail.com"));
        });
        Lobby lobby = new Lobby();
        users = userRepository.findAll().stream().filter(u -> u.getNickname().contains("Test")).limit(3).collect(Collectors.toList());
        lobby.setUsers(users);
        lobby.setActive(true);
        lobby.setCode(lobby.generatorCode());
        lobbyRepository.save(lobby);
        lobbyTest = lobbyRepository.findByCode(lobby.getCode()).orElse(null);
    }

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Lobby> lobby= lobbyRepository.findAll();

        assertNotNull(lobby);
        assertFalse(lobby.isEmpty());
        assertNotEquals(0, lobby.size());
    }

    @Test
    public void TestUpdateLobby(){
        List<Lobby> lobbies= lobbyRepository.findAll();
        Lobby lobby=lobbies.get(0);
        lobby.setActive(false);
        String code = lobby.generatorCode();
        lobby.setCode(code);
        lobbyRepository.save(lobby);
        assertNotNull(lobby);
        assertTrue(lobby.getCode().equals(code));
    }



    @Test
    public void TestFindByCode(){
        Optional<Lobby> lobby=lobbyRepository.findByCode(lobbyTest.getCode());
        assertNotNull(lobby);
    }
    

    @Test
    public void TestFindAllActiveLobby(){
        List<Lobby> lobby=lobbyRepository.findLobbyActive(true,lobbyTest.getUsers().get(0).getId());
        assertFalse(lobby.isEmpty());
    }

    @Test
    public void TestGetNumOfLobby(){
        Long numOfLobby= StreamSupport.stream(lobbyRepository.findAll().spliterator(), false).count();
        assertTrue(numOfLobby>0);
    }

}
