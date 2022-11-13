package org.springframework.samples.sevenislands.game.round;

import java.util.Collection;
import java.util.Optional;

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
    public Collection<Round> findRoundsByGameId(int id) throws DataAccessException {
        return roundRepository.findRoundByGameId(id);
    }

    @Transactional
    public void save(Round round) {
        roundRepository.save(round);
    }

    @Transactional
    public void update(Round round) {
        roundRepository.updateRound(round, round.getId());
    }

}
