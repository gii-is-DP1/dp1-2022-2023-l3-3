package sevenislands.player;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;



@DataJpaTest
public class PlayerRepositoryTest {
    
    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void initialDataAndFindSuccessTest(){
        List<Player> players = playerRepository.findAll();

        assertNotEquals(0, players.size());
        assertNull(playerRepository.findByName("sergio"));
    }
}
