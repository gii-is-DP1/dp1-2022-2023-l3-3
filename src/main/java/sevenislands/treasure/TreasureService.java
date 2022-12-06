package sevenislands.treasure;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreasureService {

    private TreasureRepository treasureRepository;

    @Autowired
    public TreasureService(TreasureRepository treasureRepository) {
        this.treasureRepository = treasureRepository;
    }

    @Transactional(readOnly = true)
    public List<Treasure> findAllTreasures() throws DataAccessException {
        return treasureRepository.findAll();
    }

    @Transactional
    public Optional<Treasure> findTreasureByName(String name) {
        return treasureRepository.findByName(name);
    }
}
