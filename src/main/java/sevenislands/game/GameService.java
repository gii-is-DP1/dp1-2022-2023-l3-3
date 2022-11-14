package sevenislands.game;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.Lobby;
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
    public void initGame(Lobby lobby) {
        Game game = new Game();
        game.setCreationDate(new Date(System.currentTimeMillis()));
        game.setLobby(lobby);
        gameRepository.save(game);

    }

    @Transactional
    public void save(Game game) {
        gameRepository.save(game);
    }

    @Transactional
    public Game findGamebByLobbyId(Integer id) {
        return gameRepository.findGamebByLobbyId(id);
    }

    @Transactional
    public void update(Game game) {
        gameRepository.updateGame(game, game.getId());
    }

}
