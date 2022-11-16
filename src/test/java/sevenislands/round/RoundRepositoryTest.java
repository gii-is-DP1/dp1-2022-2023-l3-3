package sevenislands.round;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;

@DataJpaTest
public class RoundRepositoryTest {
    

    @Autowired
    RoundRepository roundRepository;


    @Test
    public void TestDataAllFindAllSuccess(){
        List<Round> round= new ArrayList<>();
        roundRepository.findAll().iterator().forEachRemaining(round::add);

        assertNotNull(round);
        assertFalse(round.isEmpty());
        assertEquals(1, round.size());
    }


    @Test
    public void TestFindRoundByGameId(){
        List<Round> round=roundRepository.findRoundByGameId(1);

        assertNotNull(round);
        assertEquals(1, round.size());
    }
}
