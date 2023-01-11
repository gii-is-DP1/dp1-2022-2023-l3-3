package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sevenislands.card.Card;
import sevenislands.card.CardRepository;
import sevenislands.card.CardService;
import sevenislands.enums.Mode;
import sevenislands.enums.Tipo;
import sevenislands.exceptions.NotExistLobbyException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

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


@DataJpaTest
public class TurnServiceTest {
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
    public void findAllTurnTest(){
        when(turnRepository.findAll()).thenReturn(turnList);
        List<Turn> listaTurnos = turnService.findAllTurns();
        assertEquals(4, listaTurnos.size());
    }

    @Test
    public void findTurnByIdTest(){
        when(turnRepository.findById(any())).thenReturn(Optional.of(turn1));
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        Turn turno = turnService.findTurnById(1).get();
        assertNotNull(turno);
    }

    @Test
    public void findByRoundTest() throws DataAccessException {
        Round round = new Round();
        List<Turn> expectedTurns = Arrays.asList(new Turn(), new Turn());
        when(turnRepository.findByRound(round)).thenReturn(expectedTurns);
        
        List<Turn> resultTurns = turnService.findByRound(round);
        assertEquals(expectedTurns, resultTurns);
    }
    
    @Test
    @Transactional
    public void testIslandToChoose() {
        // Arrange
        Turn turn = new Turn();
        turn.setDice(3);
        Map<Card, Integer> selectedCards = new HashMap<>();
        Island island1 = new Island();
        island1.setId(1);
        
        Island island2 = new Island();
        island2.setId(2);

        Island island3 = new Island();
        island3.setId(3);
        
        Island island4 = new Island();
        island4.setId(4);
        
        List<Island> islandList = Arrays.asList(island1, island2, island3, island4);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(any())).thenReturn(selectedCards);
        List<Island> expected = Arrays.asList(island3);

        List<Island> islands = turnService.islandToChoose(turn, "nickname", islandList, request);
        assertEquals(expected, islands);

        selectedCards.put(new Card(), 1);
        when(session.getAttribute(any())).thenReturn(selectedCards);
        islands = turnService.islandToChoose(turn, "nickname", islandList, request);
        expected = Arrays.asList(island2, island4);
        assertEquals(expected, islands);
    }

    @Test
    public void saveTest(){
        when(turnRepository.save(any())).thenReturn(turn1);
        turn1.setRound(round2);
        turnService.save(turn1);
        assertEquals(turn1.getRound().getId(), turnService.save(turn1).getRound().getId());
    }

    @Test
    public void initTurnTest(){
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        List<User> userOp = List.of(user1, user2);
        List<Card> cards = List.of(card1, card2);
        Turn turnRes = turnService.initTurn(user1, round1, userOp, cards);
        assertEquals(2, turnRes.getCards().size());
        turnRes = turnService.initTurn(user1, round1, userOp, null);
        assertEquals(0, turnRes.getCards().size());
        when(turnRepository.findTurnByNickname(user2.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user2.getNickname())).collect(Collectors.toList())));
        turnRes = turnService.initTurn(user1, round1, userOp, null);
        assertEquals(1, turnRes.getCards().size());
    }

    @Test
    public void diceTest(){
        when(turnRepository.save(any())).thenReturn(turn1);
        assertEquals(null, turn1.getDice());
        turn1 = turnService.dice(turn1);
        assertNotEquals(null, turn1.getDice());
    }

    @Test
    public void asignTurnTest(){
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        when(roundRepository.findRoundsByGame(any())).thenReturn(List.of(round1, round2));
        when(turnRepository.findByRound(any())).thenReturn(List.of(turn1, turn2));

        Round round = new Round();
        when(roundRepository.save(round)).thenReturn(round);
        when(cardRepository.findByGameAndTreasure(any(), any())).thenReturn(card2);
        when(roundRepository.save(round)).thenReturn(round);
        when(cardRepository.save(any())).thenReturn(card2);
        List<Card> cards = List.of(card1,card2,card3,card4);
        when(cardRepository.findAllByGameId(any())).thenReturn(cards);

        List<User> userOp = List.of(user1, user2);
        Turn turnRes = turnService.assignTurn(user1, Optional.of(game1), userOp, List.of(round1));
        assertEquals(user2.getNickname(), turnRes.getUser().getNickname());
        
        when(roundRepository.findRoundsByGame(any())).thenReturn(List.of());
        userOp = List.of(user1, user2);
        turnRes = turnService.assignTurn(user1, Optional.of(game1), userOp, List.of(round1));
        assertEquals(user1.getNickname(), turnRes.getUser().getNickname());

        turnRes = turnService.assignTurn(user2, Optional.of(game1), userOp, List.of(round1));
        assertEquals(user2.getNickname(), turnRes.getUser().getNickname());

        when(roundRepository.findRoundsByGame(any())).thenReturn(List.of(round1, round2));
        when(turnRepository.findByRound(any())).thenReturn(List.of());
        turnRes = turnService.assignTurn(user1, Optional.of(game1), userOp, List.of(round1));
        assertNull(turnRes);
    }

    @Test
    public void refreshDeskTest(){
        List<Card> cards = List.of(card1,card2,card3,card4);
        when(cardRepository.findAllByGameId(any())).thenReturn(cards);
        when(cardRepository.save(any())).thenReturn(card2);
        when(islandRepository.findByGameId(any())).thenReturn(islands);
        Island islandTest = turnService.refreshDesk(1, user1,Optional.of(game1));
        assertNotNull(islandTest.getCard());
        when(cardRepository.findAllByGameId(any())).thenReturn(new ArrayList<>());
        islandTest = turnService.refreshDesk(1, user1,Optional.of(game1));
        assertEquals(0, islandTest.getNum());
    }

    @Test
    public void checkUserGameTest() throws NotExistLobbyException{
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        when(turnRepository.findByRound(any())).thenReturn((turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.empty());
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
        assertThrows(NotExistLobbyException.class,()-> turnService.checkUserGame(user1));
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(roundRepository.findRoundsByGame(any())).thenReturn(List.of(round1));
        Optional<Game> game = turnService.checkUserGame(user1);
        assertNotNull(game.orElse(null));
        LobbyUser lobbyUser = new LobbyUser();
        lobbyUser.setLobby(lobby1);
        lobbyUser.setUser(user1);
        game = turnService.checkUserGame(user1);
        assertNotNull(game.orElse(null));
    }

    @Test
    public void findTurnByNicknameTest(){
        List<Turn> turnos = turnService.findTurnByNickname(user1.getNickname());
        assertNotNull(turnos);
        assertEquals(0, turnos.size());
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        turnos = turnService.findTurnByNickname(user1.getNickname());
        assertEquals(1, turnos.size());
        assertEquals(turnos.get(0).getUser().getId(), user1.getId());
    }

    @Test
    public void findPlayerCardsLastTurnTest(){
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        Map<Card, Integer> mano = turnService.findPlayerCardsLastTurn(user1.getNickname());
        assertEquals(1, mano.values().size());
        assertEquals(Tipo.Caliz, mano.keySet().stream().findFirst().orElse(null).getTipo());
        assertEquals(2, mano.values().stream().findFirst().orElse(null));
    }

    @Test
    public void findPlayerCardsPenultimateTurnTest(){
        List<Card> cards = turnService.findPlayerCardsPenultimateTurn(user1.getNickname());
        assertNotNull(cards);
        assertEquals(0, cards.size());
        turn2.setUser(user1);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        cards = turnService.findPlayerCardsPenultimateTurn(user1.getNickname());
        assertEquals(1, cards.size());
        assertEquals(cards.get(0).getGame().getId(), game1.getId());
    }
    
    @Test
    public void addCardTest(){
        assertNull(turnService.addCard(1, user1));
        turn2.setUser(user1);
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobbyAndActive(any(), any())).thenReturn(Optional.of(List.of(game1)));
        when(islandRepository.findCardOfIsland(any(), any())).thenReturn(island);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        when(cardRepository.findById(any())).thenReturn(Optional.of(card1));
        Turn turn = turnService.addCard(1, user1);
        assertEquals(3, turn.getCards().size());
    }

    @Test
    public void deleteCardTest(){
        assertNull(turnService.deleteCard(1, user1.getNickname()));
        turn2.setUser(user1);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        when(cardRepository.findById(any())).thenReturn(Optional.of(card1));
        Turn turn = turnService.deleteCard(1, user1.getNickname());
        assertEquals(1, turn.getCards().size());
    }

    @Test
    public void endGameTest(){
        List<Card> cards = List.of(card1);
        when(cardRepository.findAllByGameId(any())).thenReturn(cards);
        when(islandRepository.findByGameId(any())).thenReturn(islands);
        Boolean endGame = turnService.endGame(game1);
        assertFalse(endGame);
        island.setNum(0);
        card1.setMultiplicity(0);
        endGame = turnService.endGame(game1);
        assertTrue(endGame);
    }

    @Test
    public void findTotalTurnByNicknameTest(){
        when(turnRepository.findTotalTurnsByNickname(user1.getNickname())).thenReturn((turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList()).size()));
       
        assertEquals(1, turnService.findTotalTurnsByNickname(user1.getNickname()));
    }

    //########################################



    //########################################

    //########################################



    //########################################

    //########################################



    //########################################

    //########################################



    //########################################

    //########################################



    //########################################

    //########################################



    //########################################

    //########################################



    //########################################
    



















    @Test
    public void getCardPenultimoTurnTest(){
        Card card = new Card();
        card.setId(1);
        card.setMultiplicity(10);
        card.setTipo(Tipo.Caliz);
        card.setGame(game1);
        turn1.addCard(card);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        Map<Card, Integer> mano = turnService.findPlayerCardsLastTurn(user1.getNickname());
        assertEquals(2, mano.values().size());
        assertEquals(Tipo.Caliz, mano.keySet().stream().findFirst().orElse(null).getTipo());
        assertEquals(1, mano.values().stream().findFirst().orElse(null));
    }

    // @Test
    // public void initRoundTest(){
    //     Round round = new Round();
    //     List<Card> treasureList = new ArrayList<>();
    //     when(roundRepository.save(round)).thenReturn(round);
    //     assertEquals(game1.getId(), turnService.initRound(round, Optional.of(game1), treasureList).getGame().getId());
    // }
}
