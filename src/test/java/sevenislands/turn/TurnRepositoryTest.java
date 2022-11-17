package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

        assertNotNull(turnos);
        assertNotNull(turnRepository.findById(98765));
    }
}