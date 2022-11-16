package sevenislands.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.player.Player;
import sevenislands.player.PlayerRepository;


@DataJpaTest
public class PlayerRepositoryTest {
    
    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void initialDataAndFindSuccessTest(){
        List<Player> players = playerRepository.findAll();

        assertNotEquals(0, players.size());
        assertNull(playerRepository.findByName("sergioFalso").orElse(null));
    }

    @Test
    public void findByIdAndUpdateDataTest(){
        List<Player> players = playerRepository.findAll();
        Player player1 = players.get(0);
        String oldEmail = player1.getEmail();
        player1.setEmail(oldEmail+"mod");
        playerRepository.updatePlayer(player1, player1.getId());
        assertNotNull(playerRepository.findById(player1.getId()).orElseGet(null));
        player1 = playerRepository.findById(player1.getId()).orElseGet(null);
        assertNotEquals(player1.getEmail(), oldEmail);
        
    }
}
