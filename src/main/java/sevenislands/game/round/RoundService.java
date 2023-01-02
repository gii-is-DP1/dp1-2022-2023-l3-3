package sevenislands.game.round;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoundService {

    private RoundRepository roundRepository;

    @Autowired
    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<Round> findAllRounds() throws DataAccessException {
        return roundRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Round> findRoundById(int id) throws DataAccessException {
        return roundRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Round> findRoundsByGameId(int id) throws DataAccessException {
        return roundRepository.findRoundByGameId(id).stream().collect(Collectors.toList());
    }

    @Transactional
    public void save(Round round) {
        roundRepository.save(round);
    }

    @Transactional
    public Boolean checkRoundByGameId(Integer gameId) {
        return roundRepository.findRoundByGameId(gameId).size()>0;
    }

}
