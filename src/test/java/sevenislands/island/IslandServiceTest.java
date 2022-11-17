package sevenislands.island;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import sevenislands.game.island.Island;
import sevenislands.game.island.IslandRepository;
import sevenislands.game.island.IslandService;

@ExtendWith(MockitoExtension.class)
public class IslandServiceTest {

    @Mock
    IslandRepository mock;

    Island island;
    List<Island> islandsRepo;

    private Island createIsland(Integer num){
        island = new Island();
        island.setNum(num);
        return island;
    }

    @BeforeEach
    public void config() {
        island = createIsland(1);

        islandsRepo = new ArrayList<>();
        islandsRepo.add(island);

        lenient().when(mock.findAll()).thenReturn(islandsRepo);
        lenient().when(mock.getIslandNumberById(any(Integer.class))).thenReturn(island.getNum());
    }

    @Test
    public void allIslandsFoundSuccessful() {
        IslandService islandService = new IslandService(mock);
        Iterable<Island> islandIter = islandService.findAllIslands();
        List<Island> islands = new ArrayList<>();
        islandIter.forEach(x -> islands.add(x));
        assertNotNull(islands, "El service devuelve un objeto nulo");
        assertFalse(islands.isEmpty(), "El service devuelve una colección vacía");
        assertEquals(1, islands.size(), "El service no devuelve la cantidad de islas esperada");
    }

    @Test
    public void checkIslandNumberSuccessful() {
        IslandService islandService = new IslandService(mock);
        Integer islandNumber = islandService.getIslandNumberById(1);
        assertNotNull(islandNumber, "El service devuelve un objeto nulo");
        assertTrue(islandNumber > 0, "El service devuelve un número de isla negativo");
        assertEquals(1, islandNumber, "El numero de isla no coincide con el esperado");
    }

    @Test
    public void saveTestSuccessful() {
        Island newIsland = createIsland(2);
        IslandService islandService = new IslandService(mock);
        try {
            islandService.save(newIsland);
            verify(mock).save(newIsland);
        } catch (Exception e) {
            fail("No debería saltar excepción");
        }
    }
}