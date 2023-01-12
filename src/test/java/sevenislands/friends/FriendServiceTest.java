package sevenislands.friends;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.card.Card;
import sevenislands.card.CardRepository;
import sevenislands.card.CardService;
import sevenislands.enums.Mode;
import sevenislands.enums.Status;
import sevenislands.enums.Tipo;
import sevenislands.game.Game;
import sevenislands.game.GameRepository;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandRepository;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;
import sevenislands.game.turn.TurnService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;
import sevenislands.user.friend.Friend;
import sevenislands.user.friend.FriendRepository;
import sevenislands.user.friend.FriendService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {
    @Mock
    TurnRepository turnRepository;
    
    @Mock
    UserRepository userRepository;

    @Mock
    RoundRepository roundRepository;

    @Mock
    IslandRepository islandRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    LobbyRepository lobbyRepository;

    @Mock 
    LobbyUserRepository lobbyUserRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRepository friendRepository;

    UserService userService;
    TurnService turnService;
    RoundService roundService;
    IslandService islandService;
    CardService cardService;
    GameService gameService;
    LobbyService lobbyService;
    LobbyUserService lobbyUserService;

    List<Turn> turnList = new ArrayList<>();
    Turn turn1;
    Turn turn2;
    Turn turn3;
    Turn turn4;

    User user1;
    User user2;
    User user3;
    User user4;

    Round round1;
    Round round2;
    Round round3;
    Round round4;

    Card card1;
    Card card2;
    Card card3;
    Card card4;

    Game game1;
    Game game2;

    Lobby lobby1;
    Lobby lobby2;

    Island island;
    Island island2;
    List<Island> islands;

    LobbyUser lobbyUser1;
    LobbyUser lobbyUser2;
    LobbyUser lobbyUser3;
    LobbyUser lobbyUser4;

    @BeforeEach
    public void config(){
        roundService = new RoundService(roundRepository);
        lobbyService = new LobbyService(lobbyRepository);
        lobbyUserService = new LobbyUserService(lobbyUserRepository, lobbyService);
        gameService = new GameService(roundService, gameRepository, lobbyUserService, lobbyService);
        userService = new UserService(null, null, null, userRepository, gameService, lobbyUserService);
        cardService = new CardService(cardRepository);
        islandService = new IslandService(islandRepository);
        turnService = new TurnService(turnRepository, gameService, roundService, islandService, cardService, lobbyUserService);

        user1 = userService.createUser(1, "userFalse1", "userFalse1@gmail.com");
        user2 = userService.createUser(2, "userFalse2", "userFalse2@gmail.com");
        user3 = userService.createUser(3, "userFalse3", "userFalse3@gmail.com");
        user4 = userService.createUser(4, "userFalse4", "userFalse4@gmail.com");

        lobby1 = new Lobby();
        lobby2 = new Lobby();
        lobby1.setId(1);
        lobby2.setId(2);
        lobby1.generatorCode();
        lobby2.generatorCode();
        lobby1.setActive(true);
        lobby2.setActive(true);

        lobbyUser1 = new LobbyUser();
        lobbyUser1.setLobby(lobby1);
        lobbyUser1.setUser(user1);
        lobbyUser1.setMode(Mode.PLAYER);
        lobbyUser2 = new LobbyUser();
        lobbyUser2.setLobby(lobby1);
        lobbyUser2.setUser(user2);
        lobbyUser2.setMode(Mode.PLAYER);

        lobbyUser3 = new LobbyUser();
        lobbyUser3.setLobby(lobby2);
        lobbyUser3.setUser(user3);
        lobbyUser3.setMode(Mode.PLAYER);
        lobbyUser4 = new LobbyUser();
        lobbyUser4.setLobby(lobby2);
        lobbyUser4.setUser(user4);
        lobbyUser4.setMode(Mode.PLAYER);

        game1 = new Game();
        game2 = new Game();
        game1.setId(1);
        game2.setId(2);
        game1.setActive(true);
        game1.setCreationDate( LocalDateTime.now());
        game1.setLobby(lobby1);
        game2.setActive(true);
        game2.setCreationDate( LocalDateTime.now());
        game2.setLobby(lobby2);

        round1 = new Round();
        round2 = new Round();
        round1.setId(1);
        round2.setId(2);
        round1.setGame(game1);
        round2.setGame(game1);
        round3 = new Round();
        round3.setId(3);
        round4 = new Round();
        round4.setId(4);
        round3.setGame(game2);
        round4.setGame(game2);

        card1 = new Card();
        card1.setId(1);
        card1.setMultiplicity(3);
        card1.setTipo(Tipo.Caliz);
        card1.setGame(game1);
        card2 = new Card();
        card1.setId(2);
        card2.setMultiplicity(27);
        card2.setTipo(Tipo.Doblon);
        card2.setGame(game1);
        card3 = new Card();
        card3.setId(3);
        card3.setMultiplicity(6);
        card3.setTipo(Tipo.Espada);
        card3.setGame(game2);
        card4 = new Card();
        card4.setId(4);
        card4.setMultiplicity(6);
        card4.setTipo(Tipo.BarrilRon);
        card4.setGame(game2);

        turn1 = new Turn();
        turn2 = new Turn();
        turn3 = new Turn();
        turn4 = new Turn();
        turn1.setId(1);
        turn1.setRound(round1);
        turn1.setStartTime(LocalDateTime.now());
        turn1.addCard(card1);
        turn1.addCard(card1);
        turn1.setUser(user1);
        turn2.setId(2);
        turn2.setRound(round2);
        turn2.setStartTime(LocalDateTime.now());
        turn2.addCard(card2);
        turn2.setUser(user2);
        turn3.setId(3);
        turn3.setRound(round3);
        turn3.setStartTime(LocalDateTime.now());
        turn3.addCard(card3);
        turn3.setUser(user3);
        turn4.setId(4);
        turn4.setRound(round4);
        turn4.setStartTime(LocalDateTime.now());
        turn4.addCard(card4);
        turn4.setUser(user4);

        turnList.add(turn1);
        turnList.add(turn2);
        turnList.add(turn3);
        turnList.add(turn4);

        island = new Island();
        island.setCard(card1);
        island.setGame(game1);
        island.setNum(1);
        island2 = new Island();
        island2.setCard(card2);
        island2.setGame(game1);
        island2.setNum(2);

        islands = List.of(island, island, island,island,island,island);
    }

    @Test
    @Transactional
    public void testAddFriend() {
        User userSend = new User();
        User userReceive = new User();

        friendService.addFriend(userSend, userReceive);

        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    public void testAcceptFriend() {
        User logedUser = new User();
        Friend friend = new Friend();
        friend.setId(1);
        friend.setUser1(user1);
        friend.setUser2(logedUser);
        friend.setStatus(Status.PENDING);


        when(friendRepository.findById(any())).thenReturn(Optional.of(friend));
        when(friendRepository.findByUser1AndUser2(any(), any())).thenReturn(Optional.of(friend));
        

        Optional<Friend> friends=friendService.acceptFriend(1, logedUser);
        assertTrue(friends.isPresent());
        verify(friendRepository).save(friend);
        assertEquals(Status.ACCEPTED, friend.getStatus());
    }
    

    @Test
    public void testRejectFriend() {
        
        User logedUser=user2;
        Friend friend = new Friend();
        friend.setId(1);
        friend.setUser1(user1);
        friend.setUser2(logedUser);
        friend.setStatus(Status.PENDING);
        when(friendRepository.findById(1)).thenReturn(Optional.of(friend));
        when(friendRepository.findByUser1AndUser2(any(), any())).thenReturn(Optional.of(friend));

    
        Optional<Friend> friends=friendService.rejectFriend(1, logedUser);

        assertTrue(friends.isPresent());
        verify(friendRepository).save(friend);
        assertEquals(friend.getStatus(), Status.REJECTED);
    }

    @Test
    public void testDeleteFriend() {
        User logedUser = user1;
        
        Friend friend = new Friend();
        friend.setUser1(logedUser);
        friend.setUser2(user2);
        friend.setId(1);
        when(friendRepository.findById(1)).thenReturn(Optional.of(friend));
        when(friendRepository.findByUser1AndUser2(any(), any())).thenReturn(Optional.of(friend));
        
        Optional<Friend> deletedFriend = friendService.deleteFriend(1, logedUser);
    
        assertTrue(deletedFriend.isPresent());
        assertEquals(deletedFriend.get().getId(), friend.getId());
        verify(friendRepository).delete(friend);
    }
    @Test
    public void testDeleteFriend_whenFriendNotExist_ShouldNotDeleteFriend() {
        Integer testId = 1;
        User testUser =user1;
        User testUser2 = user2;
        Friend testFriend = new Friend();
        testFriend.setId(1);
        testFriend.setUser1(testUser);
        testFriend.setUser2(testUser2);
        Friend friend2=new Friend();
        friend2.setId(2);
        when(friendRepository.findById(testId)).thenReturn(Optional.of(friend2));
        when(friendRepository.findByUser1AndUser2(any(), any())).thenReturn(Optional.of(friend2));
        Optional<Friend> deletedFriend = friendService.deleteFriend(1, testUser);
        assertTrue(deletedFriend.get().getUser2()!=user2);
       verify(friendRepository, never()).delete(testFriend);
    }
    @Test
    public void testFindFriends() {
        Friend friend1 = new Friend();
        friend1.setId(1);
        friend1.setUser1(user1);
        friend1.setUser2(user2);
        friend1.setStatus(Status.PENDING);
        Friend friend2 = new Friend();
        friend2.setId(1);
        friend2.setUser1(user1);
        friend2.setUser2(user3);
        friend2.setStatus(Status.PENDING);
        List<Friend> expectedFriends = Arrays.asList(friend1, friend2);
        when(friendRepository.findFriends(user1, Status.ACCEPTED)).thenReturn(expectedFriends);

        
        List<Friend> friends = friendService.findFriends(user1, Status.ACCEPTED);

        
        verify(friendRepository).findFriends(user1, Status.ACCEPTED);
        
        assertEquals(expectedFriends, friends);
    }

    @Test
    public void testFindFriendsTo() {
        Friend friend1 = new Friend();
        friend1.setId(1);
        friend1.setUser1(user1);
        friend1.setUser2(user2);
        friend1.setStatus(Status.PENDING);
        Friend friend2 = new Friend();
        friend2.setId(1);
        friend2.setUser1(user1);
        friend2.setUser2(user3);
        friend2.setStatus(Status.PENDING);
        List<Friend> expectedFriends = Arrays.asList(friend1, friend2);
        when(friendRepository.findFriendsTo(user1, Status.ACCEPTED)).thenReturn(expectedFriends);

        
        List<Friend> friends = friendService.findFriendsTo(user1, Status.ACCEPTED);

        
        verify(friendRepository).findFriendsTo(user1, Status.ACCEPTED);
        
        assertEquals(expectedFriends, friends);
    }

    @Test
    public void testFindFriendsFrom() {
       
        Friend friend1 = new Friend();
        friend1.setId(1);
        friend1.setUser1(user1);
        friend1.setUser2(user2);
        friend1.setStatus(Status.PENDING);
        Friend friend2 = new Friend();
        friend2.setId(1);
        friend2.setUser1(user1);
        friend2.setUser2(user3);
        friend2.setStatus(Status.PENDING);
        List<Friend> expectedFriends = Arrays.asList(friend1, friend2);
        when(friendRepository.findFriendsFrom(user1, Status.ACCEPTED)).thenReturn(expectedFriends);

        
        List<Friend> friends = friendService.findFriendsFrom(user1, Status.ACCEPTED);

        
        verify(friendRepository).findFriendsFrom(user1, Status.ACCEPTED);
        
        assertEquals(expectedFriends, friends);
    }

    @Test
    public void testFindFriendById_whenFriendExist_ShouldReturnFriend() {
        
        Friend friend1 = new Friend();
        friend1.setId(1);
        friend1.setUser1(user1);
        friend1.setUser2(user2);
        friend1.setStatus(Status.PENDING);

        when(friendRepository.findById(friend1.getId())).thenReturn(Optional.of(friend1));

        Optional<Friend> result = friendService.findFriendById(friend1.getId());

        verify(friendRepository).findById(friend1.getId());
        assertTrue(result.isPresent());
        assertEquals(friend1, result.get());
    }
    
    @Test
    public void testFindUserFriends() {
        Friend friend1 = new Friend();
        friend1.setId(1);
        friend1.setUser1(user1);
        friend1.setUser2(user2);
        friend1.setStatus(Status.PENDING);
        Friend friend2 = new Friend();
        friend2.setId(1);
        friend2.setUser1(user1);
        friend2.setUser2(user3);
        friend2.setStatus(Status.PENDING);
        List<Friend> friends = Arrays.asList(friend1, friend2);
        when(friendRepository.findFriends(user1, Status.ACCEPTED)).thenReturn(friends);

        
        List<User> userFriends = friendService.findUserFriends(user1, Status.ACCEPTED);

        
        verify(friendRepository).findFriends(user1, Status.ACCEPTED);
        
        assertEquals(Arrays.asList(user2,user3), userFriends);
    }
}
