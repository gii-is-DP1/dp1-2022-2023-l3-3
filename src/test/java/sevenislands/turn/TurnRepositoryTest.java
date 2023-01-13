package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.round.Round;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@DataJpaTest
public class TurnRepositoryTest {
    
    @Autowired
    TurnRepository turnRepository;

    @Autowired
    UserRepository userRepository;

    UserService userService;

    User user;

    @Test
    public void initialData(){
        userService = new UserService(null, null, null, userRepository, null, null);
        Round round=new Round();
        round.setId(1);
        List<Turn> turnos = turnRepository.findAll();
        List<Turn> turnosRoundId  = turnRepository.findByRound(round);
        assertNotNull(turnos);
        assertNotNull(turnosRoundId);
        assertNotNull(turnRepository.findById(98765));
    }

    @Test
    public void TestFindTurnByUser(){
        Optional<List<Turn>> turns = turnRepository.findTurnByUser(userService.findUserByNickname("player3").get());
        assertNotNull(turns.get());
    }  

}