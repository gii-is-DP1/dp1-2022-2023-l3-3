package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;

@DataJpaTest
public class TurnRepositoryTest {
    
    @Autowired
    TurnRepository turnRepository;

    @Test
    public void initialData(){
        List<Turn> turnos = turnRepository.findAll();
        List<Turn> turnosRoundId  = turnRepository.findByRoundId(1);
        assertNotNull(turnos);
        assertNotNull(turnosRoundId);
        assertNotNull(turnRepository.findById(98765));
    }

    @Test
    public void TestFindTurnByNickname(){
        Optional<List<Turn>> turns = turnRepository.findTurnByNickname("player3");
        assertNotNull(turns.get());
    }  

}