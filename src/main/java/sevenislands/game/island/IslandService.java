package sevenislands.game.island;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import sevenislands.card.Card;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IslandService {

    private IslandRepository islandRepository;

    @Autowired
    public IslandService(IslandRepository islandRepository) {
        this.islandRepository = islandRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<Island> findAllIslands() throws DataAccessException {
        return islandRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Integer getIslandNumberById(int id) throws DataAccessException {
        return islandRepository.getIslandNumberById(id);
    }

    @Transactional(readOnly = true)
    public Collection<Card> getIslandCardsById(int id) throws DataAccessException {
        return islandRepository.getIslandCardsById(id);
    }

    @Transactional
    public void save(Island island) {
        islandRepository.save(island);
    }

    @Transactional
    public void update(Island island, int island_id) {
        islandRepository.updateIsland(island, island_id);
    }
}
