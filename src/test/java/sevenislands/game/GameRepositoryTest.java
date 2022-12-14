package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;

import sevenislands.register.RegisterRepository;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired 
    UserRepository userRepository;
    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    RegisterRepository registerRepository;

    UserService userService;
    List<User> users;

    @BeforeEach
    public void config(){
        userService = new UserService(null, null, null, null, userRepository, null);
        IntStream.range(0, 3).forEach(i -> {
            userRepository.save(userService.createUser(10000+i, "playerTest"+i, "EmailTest"+i+"@gmail.com"));
        });
        Lobby lobby = new Lobby();
        users = userRepository.findAll().stream().filter(u -> u.getNickname().contains("Test")).limit(3).collect(Collectors.toList());
        lobby.setUsers(users);
        lobby.setActive(true);
        lobby.generatorCode();
        lobbyRepository.save(lobby);
        Game game = new Game();
        game.setActive(true);
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        gameRepository.save(game);
    }

    @Test
    public void checkNumOfGames() {
        List<Game> games = gameRepository.findAll();
        Long numGames = gameRepository.count();
        assertEquals(games.size(), numGames, "El repositorio no ha devuelto el n??mero de games esperado");
    }


    @Test
    public void TestFindLobbyByNicknameSuccess(){
        Optional<List<Game>> game = gameRepository.findGameByNicknameAndActive(users.get(0).getNickname(),true);
        assertNotNull(game);
    }  

    @Test
    public void TestActiveGames(){
        List<Game> gamesList = gameRepository.findGameActive(true).stream()
        .map(r -> (Game)r[0]).collect(Collectors.toList());
       assertNotEquals(0, gamesList.size());
       assertEquals(0, gamesList.stream().filter(g -> g.isActive() == false).collect(Collectors.toList()).size());
    }

}
