package sevenislands.game;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class GameService {

    
    private GameRepository gameRepository;
    private RoundService roundService;

    @Autowired
    public GameService(RoundService roundService, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.roundService = roundService;
    }

    @Transactional
<<<<<<< HEAD
    public Long gameCount() {
        return gameRepository.count();
=======
    public Integer gameCount() {
        return (int) gameRepository.count();
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
    }

    @Transactional 
    public Game initGame(Lobby lobby){
        Game game = new Game();
        game.setCreationDate(new Date(System.currentTimeMillis()));
        game.setLobby(lobby);
        game.setActive(true);
        gameRepository.save(game);
<<<<<<< HEAD
=======
        
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
        return game;
    }
    
    @Transactional
    public void save(Game game) {
         gameRepository.save(game);
    }

    @Transactional
<<<<<<< HEAD
    public Optional<Game> findGameByNickname(String nickname) {
        return gameRepository.findGameByNickname(nickname);
=======
    public Optional<Game> findGameByNickname(String nickname, Boolean active) {
        return gameRepository.findGameByNickname(nickname, active);
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
    }

    /**
     * Comprueba si el usuario está en una partida o si existe una ronda asociada a la partida en la que se
     * encuentra el usuario.
     * @param
     * @return false (en caso de que no esté en una partida, o esta esté empezada) o true (en otro caso)
     * @throws ServletException
     */
    @Transactional
    public Boolean checkUserGameWithRounds(User logedUser) {
<<<<<<< HEAD
        Optional<Game> game = gameRepository.findGameByNickname(logedUser.getNickname());
=======
        Optional<Game> game = gameRepository.findGameByNickname(logedUser.getNickname(), true);
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
        return game.isPresent() && roundService.checkRoundByGameId(game.get().getId());
     }

    @Transactional
    public List<Game> findGameActive(Boolean active) {
        return gameRepository.findGamesActive(active);
    }
}
