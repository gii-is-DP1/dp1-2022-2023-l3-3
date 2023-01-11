package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.lobby.lobbyUser.LobbyUserService;
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
    @Mock
    LobbyUserRepository lobbyUserRepository;
    @Mock
    LobbyRepository lobbyRepository;


    GameService gameService;
    RoundService roundService;
    UserService userService;
    LobbyService lobbyService;
    LobbyUserService lobbyUserService;

    List<Lobby> lobbyList = new ArrayList<>();
    List<Game> gameList = new ArrayList<>();
    List<Round> roundList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    Game game1;

    Lobby lobby1;
    Lobby lobby2;

    User user1;
    User user2;
    User user3;
    User user4;

    Round round1;
    Round round2;

    LobbyUser lobbyUser1;
    LobbyUser lobbyUser2;
    LobbyUser lobbyUser3;
    LobbyUser lobbyUser4;
    

    @BeforeEach
    private void config(){
        roundService = new RoundService(roundRepository);
        lobbyService = new LobbyService(lobbyRepository);
        lobbyUserService = new LobbyUserService(lobbyUserRepository, lobbyService);
        gameService = new GameService(roundService, gameRepository, lobbyUserService, lobbyService);
        userService = new UserService(null, null, null, userRepository, gameService, lobbyUserService);

        user1 = userService.createUser(1, "user1Test", "user1Test@gmail.com");
        user2 = userService.createUser(2, "user2Test", "user2Test@gmail.com");
        user3 = userService.createUser(3, "user3Test", "user3Test@gmail.com");
        user4 = userService.createUser(4, "user4Test", "user4Test@gmail.com");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        lobby1 = new Lobby();
        lobby2 = new Lobby();

        lobbyUser1 = new LobbyUser();
        lobbyUser1.setLobby(lobby1);
        lobbyUser1.setUser(user1);
        lobbyUser1.setMode(Mode.PLAYER);
        lobbyUser1 = new LobbyUser();
        lobbyUser1.setLobby(lobby1);
        lobbyUser1.setUser(user2);
        lobbyUser1.setMode(Mode.PLAYER);

        lobbyUser3 = new LobbyUser();
        lobbyUser3.setLobby(lobby2);
        lobbyUser3.setUser(user3);
        lobbyUser3.setMode(Mode.PLAYER);
        lobbyUser4 = new LobbyUser();
        lobbyUser4.setLobby(lobby2);
        lobbyUser4.setUser(user4);
        lobbyUser4.setMode(Mode.PLAYER);

        lobbyList.add(lobby1);
        lobbyList.add(lobby2);
        lobbyList.forEach(lobby -> gameList.add(gameService.initGame(lobby)));
        gameList.forEach(game -> game.setId(new Random().nextInt(100)));

        round1 = new Round();
        round1.setGame(gameList.get(0));
        round1.setId(1);
        round2 = new Round();
        round2.setId(2);
        round2.setGame(gameList.get(1));
        roundList.add(round1);
        roundList.add(round2);

        game1 = new Game();
        game1.setId(1);
        game1.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        game1.setCreationDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(),0, 0) );
        game1.setEndingDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(),19, 59) );
        game1.setLobby(lobby1);
    }

    @Test
    public void gameCountTest(){
        when(gameRepository.count()).thenReturn(Long.valueOf(gameList.size())); 
        assertEquals(gameList.size(), gameService.gameCount());
    }

    @Test
    public void findAllTest() {
        when(gameRepository.findAll()).thenReturn(gameList);
        assertEquals(gameList, gameService.findAll());
    }

    @Test
    public void initGameTest() {
        when(gameRepository.save(any())).thenReturn(game1);
        Game game = gameService.initGame(lobby1);
        assertEquals(game.getLobby(), lobby1);
    }

    @Test
    public void findGameByUserAndActiveTest() {
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.of(List.of(gameList.get(0))));
        assertEquals(Optional.of(gameList.get(0)), gameService.findGameByUserAndActive(user1, true));
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), gameService.findGameByUserAndActive(user1, true));
    }

    @Test
    public void findGameByUserTest() throws NotExistLobbyException {
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobby(any())).thenReturn(Optional.of(gameList.get(0)));
        assertEquals(Optional.of(gameList.get(0)), gameService.findGameByUser(user1));

        when(gameRepository.findGameByLobby(any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), gameService.findGameByUser(user1));
        
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), gameService.findGameByUser(user1));
    }

    @Test
    public void findGamePlayedByUserTest() {
        Game game = gameList.get(0);
        game.setActive(false);
        when(lobbyUserRepository.findByUserAndMode(any(), any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(lobbyUserRepository.findUsersByLobby(any())).thenReturn(List.of(user1));
        when(gameRepository.findGameActive(false)).thenReturn(List.of(game));
        Object [] object = (Object []) gameService.findGamePlayedByUser(user1).get(0);
        Pair<Game, String> result = Pair.of((Game) object[0], (String) object[1]);
        assertEquals(Pair.of(game, user1.getNickname()), result);
    }

    @Test
    public void findGameAndHostPLayedByUserTest() {
        Game game = gameList.get(0);
        game.setActive(false);
        when(lobbyUserRepository.findByUserAndMode(any(), any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(lobbyUserRepository.findUsersByLobby(any())).thenReturn(List.of(user1));
        when(gameRepository.findGameActive(false)).thenReturn(List.of(game));
        Object [] object = (Object []) gameService.findGameAndHostPLayedByUser(user1).get(0);
        Pair<Game, User> result = Pair.of((Game) object[0], (User) object[1]);
        assertEquals(Pair.of(game, user1), result);
    }

    @Test
    public void checkUserGameWithRounds() {
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.of(List.of(gameList.get(0))));
        when(roundRepository.findRoundByGame(any())).thenReturn(List.of(round1));
        assertTrue(gameService.checkUserGameWithRounds(user1));

        when(roundRepository.findRoundByGame(any())).thenReturn(new ArrayList<>());
        assertFalse(gameService.checkUserGameWithRounds(user1));

        lobbyUser1 = new LobbyUser();
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.empty());
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.empty());
        assertFalse(gameService.checkUserGameWithRounds(user1));
    }

    @Test
    public void findGameActiveTest() {
        when(gameRepository.findGameActive(any())).thenReturn(List.of(game1));
        when(lobbyUserRepository.findUsersByLobby(lobby1)).thenReturn(List.of(user1, user2));
        Object [] object = (Object []) gameService.findGameActive(true).get(0);
        Pair<Game, String> result = Pair.of((Game) object[0], (String) object[1]);
        assertEquals(Pair.of(game1, user1.getNickname()), result);
    }

    @Test
    public void endGameTest(){
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
        game1.setActive(false);
        when(gameRepository.save(any())).thenReturn(game1);
        assertFalse(gameService.endGame(user1).isActive());
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.empty());
        assertNull(gameService.endGame(user1));
    }

    @Test
    public void findTotalGamesPlayedByUserTest() {
        when(lobbyUserRepository.findByUserAndMode(any(), any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findTotalGamesPlayedByUserLobbies(any())).thenReturn(1);
        assertEquals(1, gameService.findTotalGamesPlayedByUser(user1));
    }

    @Test
    public void findGameByLobbyTest() {
        when(gameRepository.findGameByLobby(lobby1)).thenReturn(Optional.of(game1));
        assertEquals(Optional.of(game1), gameService.findGameByLobby(lobby1));
    }

    @Test
    public void findGameByUserAndModeTest() throws NotExistLobbyException {
        when(lobbyUserRepository.findByUserAndMode(user1, Mode.PLAYER)).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobby(lobby1)).thenReturn(Optional.of(game1));
        assertEquals(Optional.of(game1), gameService.findGameByUserAndMode(user1, Mode.PLAYER));
        when(lobbyUserRepository.findByUserAndMode(user1, Mode.PLAYER)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), gameService.findGameByUserAndMode(user1, Mode.PLAYER));
    }

    @Test
    public void findTotalTimePlayedByUserTest() {
        when(lobbyUserRepository.findByUserAndMode(any(), any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGamesByLobbies(any())).thenReturn(Optional.of(List.of(game1)));

        assertNotEquals(0, gameService.findTotalTimePlayedByUser(user1));
        when(gameRepository.findGamesByLobbies(any())).thenReturn(Optional.empty());
        assertEquals(0, gameService.findTotalTimePlayedByUser(user1));
    }

    @Test
    public void findTotalTimePlayedTest() {
        when(gameRepository.findAll()).thenReturn(List.of(game1));
        assertNotEquals(0, gameService.findTotalTimePlayed());
    }

    @Test
    public void findTotalGamesPlayedByDayTest() {
        when(gameRepository.findTotalGamesPlayedByDay()).thenReturn(List.of(gameList.size()));
        assertNotEquals(2, gameService.findTotalGamesPlayedByDay());
    }

    @Test
    public void findAverageGamesPlayedTest() {
        when(gameRepository.count()).thenReturn(2L);
        when(gameRepository.findTotalGamesPlayedByDay()).thenReturn(List.of(gameList.size()));
        assertEquals(2, gameService.findAverageGamesPlayed());
    }

    @Test
    public void findMaxGamesPlayedADayTest() {
        when(gameRepository.findTotalGamesPlayedByDay()).thenReturn(List.of(gameList.size(), 1));
        assertEquals(2, gameService.findMaxGamesPlayedADay());
    }

    @Test
    public void findMinGamesPlayedADayTest() {
        when(gameRepository.findTotalGamesPlayedByDay()).thenReturn(List.of(gameList.size(), 1));
        assertEquals(1, gameService.findMinGamesPlayedADay());
    }

    @Test
    public void checkUserGameTest() throws NotExistLobbyException{
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobby(any())).thenReturn(Optional.of(game1));
        assertTrue(gameService.checkUserGame(user1));
        game1.setActive(false);
        when(gameRepository.findGameByLobby(any())).thenReturn(Optional.empty());
        assertFalse(gameService.checkUserGame(user1));
    }

    @Test
    public void findVictoriesTest() {
        List<Object []> list = new ArrayList<>();
        list.add(new Object [] {user1.getNickname(), 1});
        when(gameRepository.findVictories()).thenReturn(list);
        assertEquals(list, gameService.findVictories());
    }

    // @Test
    // public void checkNickNameANDGameIdTest(){
    //     Integer id = gameList.get(0).getId();
    //     when(gameRepository.findGameByNicknameAndActive(userList.get(0).getNickname(),true)).thenReturn(Optional.of(gameList));
    //     when(roundRepository.findRoundByGameId(id)).thenReturn(roundList.stream().filter(r -> r.getGame().getId().equals(id)).collect(Collectors.toList()));
    //     assertNotNull(gameService.checkUserGameWithRounds(userList.get(0)));
    // }

    // @Test
    // public void checkGameActiveTest(){
    //     when(gameRepository.findGameActive(true).stream()
    //     .map(r -> (Game)r[0]).collect(Collectors.toList())).thenReturn(gameList.stream().filter(g->g.isActive()).collect(Collectors.toList()));
        
    //     assertEquals(2, gameService.findGameActive(true).size());
    //     gameList.get(1).setActive(false);
    //     when(gameRepository.findGameActive(false).stream()
    //     .map(r -> (Game)r[0]).collect(Collectors.toList())).thenReturn(gameList.stream().filter(g->g.isActive()==false).collect(Collectors.toList()));

    //     assertEquals(1, gameService.findGameActive(false).size());
    // }
    
    // @Test
    // public void findTotalTimePlayedTest(){
    //     assertTrue(gameService.findTotalTimePlayed()==0);
    //     when(gameRepository.findAll()).thenReturn((List.of(game1)));
    //     assertTrue(gameService.findTotalTimePlayed()>0);
    // }

    // @Test
    // public void findGamesByNicknameAndActiveTest(){
    //     assertFalse(gameService.findGamesByNicknameAndActive(user1.getNickname(), true).isPresent());
    //     when(gameRepository.findGameByNicknameAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
    //     assertFalse(gameService.findGamesByNicknameAndActive(user1.getNickname(), true).orElse(null).isEmpty());
    // }

    // @Test
    // public void findTotalGamesPlayedByNicknameTest(){
    //     when(gameRepository.findTotalGamesPlayedByNickname(any())).thenReturn(gameList.size());
    //     assertFalse(gameService.findTotalGamesPlayedByNickname(user1.getNickname())==0);
    // }



}
