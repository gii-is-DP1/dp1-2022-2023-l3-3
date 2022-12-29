package sevenislands.statistic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.GameRepository;

@Service
public class StatisticService {
    
    private GameRepository gameRepository;
    
    @Autowired
    public StatisticService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Integer numGamesNickname(String nickname) {
        return gameRepository.numGamesNickname(nickname);
    }

}
