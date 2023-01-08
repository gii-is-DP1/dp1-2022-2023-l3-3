package sevenislands.gameDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sevenislands.achievement.AchievementRepository;
import sevenislands.achievement.AchievementService;
import sevenislands.card.Card;
import sevenislands.card.CardRepository;
import sevenislands.card.CardService;
import sevenislands.enums.Tipo;
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


    GameService gameService;
    GameDetailsService gameDetailsService;
    UserService userService;
    TurnService turnService;
    RoundService roundService;
    IslandService islandService;
    CardService cardService;
    LobbyService lobbyService;

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

    @BeforeEach
    public void config(){
        roundService = new RoundService(roundRepository);
        lobbyService = new LobbyService(lobbyRepository, gameService);
        gameService = new GameService(roundService, gameRepository);
        cardService = new CardService(cardRepository);
        islandService = new IslandService(islandRepository);
        userService = new UserService(null, lobbyService, null, null, userRepository, gameService);
        turnService = new TurnService(turnRepository, gameService, roundService, lobbyService, islandService, cardService);
        gameDetailsService = new GameDetailsService(gameDetailsRepository, gameService, turnService);

        user1 = userService.createUser(1, "userFalse1", "userFalse1@gmail.com");
        user2 = userService.createUser(2, "userFalse2", "userFalse2@gmail.com");

        lobby1 = new Lobby();
        lobby1.setId(1);
        lobby1.generatorCode();
        lobby1.setActive(true);
        lobby1.addPlayer(user1);
        lobby1.addPlayer(user2);

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
    public void calculateDetailsTest(){
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList())));
        when(turnRepository.findTurnByNickname(user2.getNickname())).thenReturn(Optional.of(turnList.stream().filter(t-> t.getUser().getNickname().equals(user2.getNickname())).collect(Collectors.toList())));
        when(gameRepository.findGameByNickname(any())).thenReturn(Optional.of(List.of(game1)));
        List<GameDetails> gameDetails = gameDetailsService.calculateDetails(user1);
        assertNotNull(gameDetails);
        assertEquals(user1.getNickname(), gameDetails.get(0).getUser().getNickname());
        turn2.addCard(card2);
        gameDetails = gameDetailsService.calculateDetails(user1);
        assertNotNull(gameDetails);
        assertTrue(gameDetails.get(0).getPunctuation()==2);
    }

}
