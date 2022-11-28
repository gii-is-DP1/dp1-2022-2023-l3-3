package sevenislands.turn;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnRepository;
import sevenislands.game.turn.TurnService;


@DataJpaTest
public class TurnServiceTest {
    
    @Autowired
    TurnRepository turnRepository;


    @Test
    public void getAllTurnTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null);
        List<Turn> listaTurnos = turnService.findAllTurns();
        assertNotEquals(0, listaTurnos.size());
    }

    @Test
    public void getTurnByIdTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null);
        Turn turno = turnService.findTurnById(1).get();
        assertNotNull(turno);
    }

    @Test
    public void getTurnByRoundIdTest(){
        TurnService turnService = new TurnService(turnRepository, null, null, null);
        List<Turn> turnos = turnService.findByRoundId(1);
        assertNotNull(turnos);
    }


}
