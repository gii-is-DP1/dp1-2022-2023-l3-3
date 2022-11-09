package org.springframework.samples.sevenislands.game;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.Lobby;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public int gameCount() {
        return (int) gameRepository.count();
    }

    @Transactional 
    public void initGame(Lobby lobby){
        Game game = new Game();
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        gameRepository.save(game);

    }
}
