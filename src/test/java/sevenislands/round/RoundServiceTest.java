package sevenislands.round;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.Game;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;

@DataJpaTest
public class RoundServiceTest {
    

    @Autowired
    private RoundRepository roundRepository;

   


    @Test
    public void TestFindAllRounds(){
        RoundService roundService=new RoundService(roundRepository);
        Iterable<Round> round=roundService.findAllRounds();
        List<Round>rondas=new ArrayList<>();
        round.iterator().forEachRemaining(rondas::add);
        assertNotNull(rondas);
    }

    @Test 
    public void TestFindRoundById(){
        RoundService roundService=new RoundService(roundRepository);
        Optional<Round> ronda=roundService.findRoundById(1);
        assertNotNull(ronda.get());
    }

    @Test
    public void TestFindRoundsByGameId(){
        RoundService roundService=new RoundService(roundRepository);
        Collection<Round> rondas=roundService.findRoundsByGameId(1);
        assertNotNull(rondas.stream().collect(Collectors.toList()));
    }


    @Test
    public void TestSave(){
        RoundService roundService=new RoundService(roundRepository);
        Round ronda=new Round();
        Game game=new Game();
        LocalDateTime fecha = LocalDateTime.now();
        game.setCreationDate(fecha);
        ronda.setGame(game);
        roundService.save(ronda);
        assertNotNull(ronda);
        assertTrue(ronda.getId().equals(16));
    }


    @Test
    public void TestCheckNoRoundByGameId(){
        RoundService roundService=new RoundService(roundRepository);
        Boolean gameWithoutRound=roundService.checkRoundByGameId(1);
        assertNotNull(gameWithoutRound);
        assertTrue(gameWithoutRound);

    }

}
