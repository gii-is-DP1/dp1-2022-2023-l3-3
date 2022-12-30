package sevenislands.statistic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.GameRepository;
import sevenislands.game.turn.TurnRepository;

@Service
public class StatisticService {
    
    private GameRepository gameRepository;
    private TurnRepository turnRepository;
    
    @Autowired
    public StatisticService(GameRepository gameRepository, TurnRepository turnRepository) {
        this.gameRepository = gameRepository;
        this.turnRepository = turnRepository;
    }

    @Transactional
    public Integer numGamesNickname(String nickname) {
        return gameRepository.numGamesNickname(nickname);
    }

    @Transactional
    public Integer numTurnsNickname(String nickname) {
        return turnRepository.numTurnsNickname(nickname);
    }

}
