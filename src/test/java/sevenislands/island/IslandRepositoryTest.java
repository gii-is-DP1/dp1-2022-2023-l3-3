package sevenislands.island;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.island.Island;
import sevenislands.game.island.IslandRepository;

@DataJpaTest
public class IslandRepositoryTest {
    
    @Autowired
    IslandRepository islandRepository;

    @Test
    public void getIslandNumberSuccessful() {
        Integer num = islandRepository.getIslandNumberById(1);
        assertEquals(1, num, "La isla debería tener el numero 1");
    }

    @Test
    public void changeIslandInfo() {
        Island island = islandRepository.findById(1).get();
        Integer cambio = 2;
        island.setNum(cambio);
        islandRepository.updateIsland(island, island.getId());

        Island newIsland = islandRepository.findById(1).get();
        assertEquals(cambio, newIsland.getNum());
    }
}
