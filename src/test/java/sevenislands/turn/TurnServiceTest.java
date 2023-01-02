package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import sevenislands.card.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.geo.GeoModule;

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
    
    List<Turn> turnList;
    Turn turn1;
    Turn turn2;
    User user1;
    User user2;
    User user3;
    User user4;
    Round round1;
    Round round2;
    Card card1;
    Card card2;
    Game game1;
    Game game2;
    Lobby lobby1;
    Lobby lobby2;

    @BeforeEach
    public void config(){
        turn1= new Turn();
        turn2= new Turn();

        userService = new UserService(null, null, null, null, userRepository);
        user1 = userService.createUser(1, "userFalse1", "userFalse1@gmail.com");
        user2 = userService.createUser(2, "userFalse2", "userFalse2@gmail.com");
        user3 = userService.createUser(3, "userFalse3", "userFalse3@gmail.com");
        user4 = userService.createUser(4, "userFalse4", "userFalse4@gmail.com");

        lobby1 = new Lobby();
        lobby2 = new Lobby();
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
        game1.setActive(true);
        game1.setCreationDate( new Date(System.currentTimeMillis()));
        round1 = new Round();
    }

    @Test
    public void getAllTurnTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        List<Turn> listaTurnos = turnService.findAllTurns();
        assertNotEquals(0, listaTurnos.size());
    }

    @Test
    public void getTurnByIdTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        Turn turno = turnService.findTurnById(1).get();
        assertNotNull(turno);
    }

    @Test
    public void getTurnByRoundIdTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null, null, null);
        List<Turn> turnos = turnService.findByRoundId(1);
        assertNotNull(turnos);
    }


}
