package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

    Game game1;

    Lobby lobby;
    Lobby lobby2;

    User user1;
    User user2;
    User user3;
    User user4;

    Round round;
    Round round2;
    

    @BeforeEach
    private void config(){
        roundService = new RoundService(roundRepository);
        gameService = new GameService(roundService, gameRepository);
        userService = new UserService(null, null, null, null, userRepository, null);
        userService = new UserService(null, lobbyService, null, null, null, null);
        user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        user2 = userService.createUser(2, "user2Test", "user2Test@gmail.com");
        user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        lobby = new Lobby();
        lobby2 = new Lobby();
        lobby.addPlayer(user1);
        lobby.addPlayer(user2);
        lobby2.addPlayer(user3);
        lobby2.addPlayer(user4);
        lobbyList.add(lobby);
        lobbyList.add(lobby2);
        lobbyList.forEach(l -> gameList.add(gameService.initGame(l)));
        gameList.forEach(g -> g.setId(new Random().nextInt(100)));
        round = new Round();
        round.setGame(gameList.get(0));
        round.setId(1);
        round2 = new Round();
        round2.setId(2);
        round2.setGame(gameList.get(1));
        roundList.add(round);
        roundList.add(round2);

        game1 = new Game();
        game1.setId(1);
        game1.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        game1.setCreationDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(),0, 0) );
        game1.setEndingDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(),19, 59) );
        game1.setLobby(lobby);
    }

    @Test
    public void GameCountTest(){
        gameService = new GameService(roundService, gameRepository);
        when(gameRepository.count()).thenReturn(Long.valueOf(gameList.size())); 
        assertEquals(gameList.size(), gameService.gameCount());
    }

    @Test
    public void findGameByNickNameTest(){
        assertFalse(gameService.findGameByNicknameAndActive("user1Test",true).isPresent());
        when(gameRepository.findGameByNicknameAndActive("user1Test",true)).thenReturn(Optional.of(gameList));
        assertNotNull(gameService.findGameByNicknameAndActive("user1Test",true));
    }

    @Test
    public void checkNickNameANDGameIdTest(){
        Integer id = gameList.get(0).getId();
        when(gameRepository.findGameByNicknameAndActive(userList.get(0).getNickname(),true)).thenReturn(Optional.of(gameList));
        when(roundRepository.findRoundByGameId(id)).thenReturn(roundList.stream().filter(r -> r.getGame().getId().equals(id)).collect(Collectors.toList()));
        assertNotNull(gameService.checkUserGameWithRounds(userList.get(0)));
    }

    @Test
    public void checkGameActiveTest(){
        when(gameRepository.findGameActive(true).stream()
        .map(r -> (Game)r[0]).collect(Collectors.toList())).thenReturn(gameList.stream().filter(g->g.isActive()).collect(Collectors.toList()));
        
        assertEquals(2, gameService.findGameActive(true).size());
        gameList.get(1).setActive(false);
        when(gameRepository.findGameActive(false).stream()
        .map(r -> (Game)r[0]).collect(Collectors.toList())).thenReturn(gameList.stream().filter(g->g.isActive()==false).collect(Collectors.toList()));

        assertEquals(1, gameService.findGameActive(false).size());
    }
    
    @Test
    public void findTotalTimePlayedTest(){
        assertTrue(gameService.findTotalTimePlayed()==0);
        when(gameRepository.findAll()).thenReturn((List.of(game1)));
        assertTrue(gameService.findTotalTimePlayed()>0);
    }

    @Test
    public void endGameTest(){
        when(gameRepository.findGameByNicknameAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
        assertFalse(gameService.endGame(user1).orElse(null).isActive());
    }

    @Test
    public void checkUserGameTest(){
        when(gameRepository.findGameByNickname(any())).thenReturn(Optional.of(List.of(game1)));
        assertTrue(gameService.checkUserGame(user1));
        game1.setActive(false);
        assertFalse(gameService.checkUserGame(user1));
    }

    @Test
    public void findGamesByNicknameAndActiveTest(){
        assertFalse(gameService.findGamesByNicknameAndActive(user1.getNickname(), true).isPresent());
        when(gameRepository.findGameByNicknameAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
        assertFalse(gameService.findGamesByNicknameAndActive(user1.getNickname(), true).orElse(null).isEmpty());
    }

    @Test
    public void findTotalGamesPlayedByNicknameTest(){
        when(gameRepository.findTotalGamesPlayedByNickname(any())).thenReturn(gameList.size());
        assertFalse(gameService.findTotalGamesPlayedByNickname(user1.getNickname())==0);
    }



}
