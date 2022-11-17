package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;
import sevenislands.user.User;

@DataJpaTest
public class TurnServiceTest {
    
    @Autowired
    TurnRepository turnRepository;


    @Test
    public void getAllTurnTest(){
        List<Turn> listaTurnos = turnRepository.findAll();
        assertNotEquals(0, listaTurnos.size());
    }

    @Test
    public void getTurnByIdTest(){
        Turn turno = turnRepository.findById(1).get();
        assertNotNull(turno);
    }

    @Test
    public void getTurnByRoundIdTest(){
        List<Turn> turnos = turnRepository.findByRoundId(1);
        assertNotNull(turnos);
    }


}
