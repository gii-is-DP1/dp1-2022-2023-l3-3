package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    GameRepository gameRepository;
    @Mock
    RoundRepository roundRepository;
    @Mock
    UserRepository userRepository;

    GameService gameService;
    RoundService roundService;
    UserService userService;
    LobbyService lobbyService;

    List<Lobby> lobbyList = new ArrayList<>();
    List<Game> gameList = new ArrayList<>();
    List<Round> roundList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    @BeforeEach
    private void config(){
        roundService = new RoundService(roundRepository);
        gameService = new GameService(roundService, gameRepository);
        userService = new UserService(null, null, null, null, userRepository);
        userService = new UserService(null, lobbyService, null, null, null);
        User user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        User user2 = userService.createUser(2, "user2Test", "user2Test@gmail.com");
        User user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        User user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        Lobby lobby = new Lobby();
        Lobby lobby2 = new Lobby();
        lobby.addPlayer(user1);
        lobby.addPlayer(user2);
        lobby2.addPlayer(user3);
        lobby2.addPlayer(user4);
        lobbyList.add(lobby);
        lobbyList.add(lobby2);
        lobbyList.forEach(l -> gameList.add(gameService.initGame(l)));
        gameList.forEach(g -> g.setId(new Random().nextInt(100)));
        Round round = new Round();
        round.setGame(gameList.get(0));
        round.setId(1);
        Round round2 = new Round();
        round2.setId(2);
        round2.setGame(gameList.get(1));
        roundList.add(round);
        roundList.add(round2);
    }

    @Test
    public void GameCountTest(){
        gameService = new GameService(roundService, gameRepository);
        when(gameRepository.count()).thenReturn(Long.valueOf(gameList.size())); 
        assertEquals(gameList.size(), gameService.gameCount());
    }

    @Test
    public void findGameByNickNameTest(){
        when(gameRepository.findGameByNickname("user1Test",true)).thenReturn(Optional.of(gameList.get(0)));
        assertNotNull(gameService.findGameByNickname("user1Test",true));
    }

    @Test
    public void checkNickNameANDGameIdTest(){
        Integer id = gameList.get(0).getId();
        when(gameRepository.findGameByNickname(userList.get(0).getNickname(),true)).thenReturn(Optional.of(gameList.get(0)));
        when(roundRepository.findRoundByGameId(id)).thenReturn(roundList.stream().filter(r -> r.getGame().getId().equals(id)).collect(Collectors.toList()));
        assertNotNull(gameService.checkUserGameWithRounds(userList.get(0)));
    }

    @Test
    public void checkGameActiveTest(){
        when(gameRepository.findGamesActive(true)).thenReturn(gameList.stream().filter(g->g.isActive()).collect(Collectors.toList()));
        
        assertEquals(2, gameService.findGameActive(true).size());
        gameList.get(1).setActive(false);
        when(gameRepository.findGamesActive(false)).thenReturn(gameList.stream().filter(g->g.isActive()==false).collect(Collectors.toList()));

        assertEquals(1, gameService.findGameActive(false).size());
    }
    
}
