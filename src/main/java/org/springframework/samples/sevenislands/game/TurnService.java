package org.springframework.samples.sevenislands.game;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnService {

    private TurnRepository turnRepository;

    @Autowired
    public TurnService(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    @Transactional(readOnly = true)
    public Collection<Turn> findAllTurns() throws DataAccessException {
        return turnRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Turn> findTurnById(int id) throws DataAccessException {
        return turnRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Collection<Turn> findByRoundId(int id) throws DataAccessException {
        return turnRepository.findByRoundId(id);
    }

    @Transactional()
    public void save(Turn turn) {
        turnRepository.save(turn);
    }

}
