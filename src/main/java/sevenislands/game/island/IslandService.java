package sevenislands.game.island;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

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
    public List<Island> findAllIslands() throws DataAccessException {
        return StreamSupport.stream(islandRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
    @Transactional
    public Island findByCardId(Integer cardId){
        return islandRepository.findByCardId(cardId);
    }

    @Transactional
    public Island findCardOfIsland(Integer gameId, Integer numIsland){
        return islandRepository.findCardOfIsland(gameId, numIsland);
    }

    @Transactional(readOnly = true)
    public Integer getIslandNumberById(Integer id) throws DataAccessException {
        return islandRepository.getIslandNumberById(id);
    }
    @Transactional(readOnly = true)
    public Island findIslandById(Integer id) throws DataAccessException {
        return islandRepository.findById(id).get();
    }

    @Transactional
    public Island save(Island island) {
        return islandRepository.save(island);
    }

    @Transactional
    public List<Island> findIslandsByGameId(Integer gameId) {
        return islandRepository.findByGameId(gameId);
    }
}
