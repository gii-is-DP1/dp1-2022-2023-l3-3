package sevenislands.game;

import java.util.List;
import java.util.Optional;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public Integer gameCount() {
        return (int) gameRepository.count();
    }

    @Transactional 
    public Game initGame(Lobby lobby){
        Game game = new Game();
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        game.setActive(true);
        gameRepository.save(game);
        
        return game;
    }
    
    @Transactional
    public void save(Game game) {
         gameRepository.save(game);
    }

    @Transactional
    public Optional<List<Game>> findGamesByNicknameAndActive(String nickname, Boolean active) {
        Optional<List<Game>> gameList = gameRepository.findGameByNicknameAndActive(nickname, active);
        if(gameList.isPresent()) {
            return gameList;
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Game> findGameByNicknameAndActive(String nickname, Boolean active) {
        Optional<List<Game>> gameList = gameRepository.findGameByNicknameAndActive(nickname, active);
        if(gameList.isPresent()) {
            return Optional.of(gameList.get().get(0));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Game> findGameByNickname(String nickname) {
        Optional<List<Game>> games = gameRepository.findGameByNickname(nickname);
        if(games.isPresent()) {
            return Optional.of(games.get().get(0));
        }
        return Optional.empty();
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
        Optional<Game> game = findGameByNicknameAndActive(logedUser.getNickname(), true);
        return game.isPresent() && roundService.checkRoundByGameId(game.get().getId());
     }

    @Transactional
    public List<Game> findGameActive(Boolean active) {
        return gameRepository.findGamesActive(active);
    }

    @Transactional
    public void endGame(User logedUser) {
        Optional<Game> game = findGameByNicknameAndActive(logedUser.getNickname(), true);
        if(game.isPresent()) {
            game.get().setActive(false);
            game.get().setEndingDate(LocalDateTime.now());
            gameRepository.save(game.get());
        }
    }

    @Transactional
    public Integer findTotalGamesPlayedByNickname(String nickname) {
        return gameRepository.totalGamesPlayedByNickname(nickname);
    }

    @Transactional
    public Long findTotalTimePlayed(String nickname) {
        Optional<List<Game>> games = gameRepository.findGameByNickname(nickname);
        Duration played = Duration.ZERO;
        if(games.isPresent()){
            for(Game g : games.get()) {
                LocalDateTime creationDate = g.getCreationDate();
                LocalDateTime endingDate = g.getEndingDate();
                Duration diference = Duration.between(creationDate,endingDate);
                played = played.plus(diference);
            }
        }
        return played.toMinutes();   
    }
    
    public Boolean checkUserGame(User logedUser) {
        Optional<Game> game = findGameByNickname(logedUser.getNickname());
        if(game.isPresent() && game.get().isActive()) return true;
        return false;
    }
}
