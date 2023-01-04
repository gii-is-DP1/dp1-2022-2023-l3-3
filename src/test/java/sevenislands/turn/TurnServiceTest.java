package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import sevenislands.card.Card;
import sevenislands.enums.Tipo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.Game;
import sevenislands.game.round.Round;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;
import sevenislands.game.turn.TurnService;
import sevenislands.lobby.Lobby;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;


@DataJpaTest
public class TurnServiceTest {
    
    @Mock
    TurnRepository turnRepository;
    @Mock
    UserRepository userRepository;

    UserService userService;
    TurnService turnService;
    
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

    @BeforeEach
    public void config(){
        userService = new UserService(null, null, null, null, userRepository);
        turnService = new TurnService(turnRepository, null, null, null, null, null);

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
        lobby1.addPlayer(user1);
        lobby1.addPlayer(user2);
        lobby2.addPlayer(user3);
        lobby2.addPlayer(user4);

        game1 = new Game();
        game2 = new Game();
        game1.setId(1);
        game2.setId(2);
        game1.setActive(true);
        game1.setCreationDate( new Date(System.currentTimeMillis()));
        game1.setLobby(lobby1);
        game2.setActive(true);
        game2.setCreationDate( new Date(System.currentTimeMillis()));
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
        card1.setMultiplicity(10);
        card1.setTipo(Tipo.Caliz);
        card1.setGame(game1);
        card2 = new Card();
        card1.setId(2);
        card2.setMultiplicity(10);
        card2.setTipo(Tipo.Doblon);
        card2.setGame(game1);
        card3 = new Card();
        card3.setId(3);
        card3.setMultiplicity(10);
        card3.setTipo(Tipo.Caliz);
        card3.setGame(game2);
        card4 = new Card();
        card4.setId(4);
        card4.setMultiplicity(10);
        card4.setTipo(Tipo.Doblon);
        card4.setGame(game2);

        turn1 = new Turn();
        turn2 = new Turn();
        turn3 = new Turn();
        turn4 = new Turn();
        turn1.setId(1);
        turn1.setRound(round1);
        turn1.setStartTime(LocalDateTime.now());
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
        

    }

    @Test
    public void getAllTurnTest(){
        turnService = new TurnService(turnRepository, null, null, null, null, null);

        when(turnRepository.findAll()).thenReturn(turnList);
        List<Turn> listaTurnos = turnService.findAllTurns();
        assertEquals(4, listaTurnos.size());
    }

    @Test
    public void getTurnByIdTest(){
        when(turnRepository.findById(any())).thenReturn(Optional.of(turn1));
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        Turn turno = turnService.findTurnById(1).get();
        assertNotNull(turno);
    }

    @Test
    public void getTurnByRoundIdTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.findByRoundId(any())).thenReturn(turnList.stream().filter(t-> t.getRound().getId().equals(1)).collect(Collectors.toList()));
        List<Turn> turnos = turnService.findByRoundId(1);
        assertEquals(1, turnos.size());;
        assertEquals(turn1, turnos.get(0));
    }

    @Test
    public void getTurnByNicknameTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList()));
        List<Turn> turnos = turnService.findTurnByNickname(user1.getNickname());
        assertEquals(1, turnos.size());
        assertEquals(turnos.get(0).getUser().getId(), user1.getId());
    }

    @Test
    public void getTurnSaveTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.save(any())).thenReturn(turn1);
        turn1.setRound(round2);
        turnService.save(turn1);
        assertEquals(turn1.getRound().getId(), turnService.save(turn1).getRound().getId());
    }

    @Test
    public void getTurnDiceTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.save(any())).thenReturn(turn1);
        assertEquals(null, turn1.getDice());
        turn1 = turnService.dice(turn1);
        assertNotEquals(null, turn1.getDice());
    }

    @Test
    public void getCardLastTurnTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList()));
        Map<Card, Integer> mano = turnService.findPlayerCardsLastTurn(user1.getNickname());
        assertEquals(1, mano.values().size());
        assertEquals(Tipo.Caliz, mano.keySet().stream().findFirst().orElse(null).getTipo());
        assertEquals(1, mano.values().stream().findFirst().orElse(null));
    }

    @Test
    public void getCardPenultimoTurnTest(){

        Card card = new Card();
        card.setId(1);
        card.setMultiplicity(10);
        card.setTipo(Tipo.Caliz);
        card.setGame(game1);
        turn1.addCard(card);
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        when(turnRepository.findTurnByNickname(user1.getNickname())).thenReturn(turnList.stream().filter(t-> t.getUser().getNickname().equals(user1.getNickname())).collect(Collectors.toList()));
        Map<Card, Integer> mano = turnService.findPlayerCardsLastTurn(user1.getNickname());
        assertEquals(2, mano.values().size());
        assertEquals(Tipo.Caliz, mano.keySet().stream().findFirst().orElse(null).getTipo());
        assertEquals(1, mano.values().stream().findFirst().orElse(null));
    }


}
