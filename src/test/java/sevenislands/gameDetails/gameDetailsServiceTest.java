package sevenislands.gameDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sevenislands.card.Card;
import sevenislands.card.CardRepository;
import sevenislands.card.CardService;
import sevenislands.enums.Mode;
import sevenislands.enums.Tipo;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameRepository;
import sevenislands.game.GameService;
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

@ExtendWith(MockitoExtension.class)
public class gameDetailsServiceTest {
    
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
    LobbyRepository lobbyRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    GameDetailsRepository gameDetailsRepository;
    @Mock
    LobbyUserRepository lobbyUserRepository;

    GameService gameService;
    GameDetailsService gameDetailsService;
    UserService userService;
    TurnService turnService;
    RoundService roundService;
    IslandService islandService;
    CardService cardService;
    LobbyService lobbyService;
    LobbyUserService lobbyUserService;

    User user1;
    User user2;

    Card card1;
    Card card2;
    Card card3;
    Card card4;

    Game game1;

    Lobby lobby1;

    List<Turn> turnList = new ArrayList<>();
    Turn turn1;
    Turn turn2;

    Round round1;
    Round round2;

    LobbyUser lobbyUser1;
    LobbyUser lobbyUser2;

    @BeforeEach
    public void config(){
        roundService = new RoundService(roundRepository);
        lobbyService = new LobbyService(lobbyRepository);
        lobbyUserService = new LobbyUserService(lobbyUserRepository, lobbyService);
        gameService = new GameService(roundService, gameRepository, lobbyUserService, lobbyService);
        cardService = new CardService(cardRepository);
        islandService = new IslandService(islandRepository);
        userService = new UserService(null, null, null, userRepository, gameService, lobbyUserService);
        turnService = new TurnService(turnRepository, gameService, roundService, islandService, cardService, lobbyUserService);
        gameDetailsService = new GameDetailsService(gameDetailsRepository, gameService, turnService, lobbyUserService);

        user1 = userService.createUser(1, "userFalse1", "userFalse1@gmail.com");
        user2 = userService.createUser(2, "userFalse2", "userFalse2@gmail.com");
        lobby1 = new Lobby();
        lobby1.setId(1);
        lobby1.generatorCode();
        lobby1.setActive(true);
        
        lobbyUser1 = new LobbyUser();
        lobbyUser1.setLobby(lobby1);
        lobbyUser1.setUser(user1);
        lobbyUser1.setMode(Mode.PLAYER);
        lobbyUser2 = new LobbyUser();
        lobbyUser2.setLobby(lobby1);
        lobbyUser2.setUser(user2);
        lobbyUser2.setMode(Mode.PLAYER);

        game1 = new Game();
        game1.setId(1);
        game1.setActive(true);
        game1.setCreationDate( LocalDateTime.now());
        game1.setLobby(lobby1);

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
        card3.setGame(game1);
        card4 = new Card();
        card4.setId(4);
        card4.setMultiplicity(6);
        card4.setTipo(Tipo.BarrilRon);
        card4.setGame(game1);

        round1 = new Round();
        round2 = new Round();
        round1.setId(1);
        round2.setId(2);
        round1.setGame(game1);
        round2.setGame(game1);
        
        turn1 = new Turn();
        turn2 = new Turn();
     
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
        
        turnList.add(turn1);
        turnList.add(turn2);
    }

    @Test
    public void calculateDetailsTest() throws NotExistLobbyException {
        when(lobbyUserRepository.findByUser(user1)).thenReturn(Optional.of(List.of(lobbyUser1)));
        when(gameRepository.findGameByLobby(lobby1)).thenReturn(Optional.of(game1));
        when(lobbyUserRepository.findUsersByLobbyAndMode(lobby1, Mode.PLAYER)).thenReturn(List.of(user1, user2));
        when(turnRepository.findTurnByUser(user1)).thenReturn(Optional.of(List.of(turnList.get(0))));
        when(turnRepository.findTurnByUser(user2)).thenReturn(Optional.of(List.of(turnList.get(1))));
        
        List<GameDetails> gameDetails = gameDetailsService.calculateDetails(user1);
        assertNotNull(gameDetails);
        assertEquals(user1.getNickname(), gameDetails.get(0).getUser().getNickname());
        turn2.addCard(card2);
        gameDetails = gameDetailsService.calculateDetails(user1);
        assertNotNull(gameDetails);
        assertTrue(gameDetails.get(0).getPunctuation()==2);
    }
}
