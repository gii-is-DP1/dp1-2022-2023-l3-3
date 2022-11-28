package sevenislands.game.turn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;

@Service
public class TurnService {

    private TurnRepository turnRepository;
    private final GameService gameService;
    private final RoundService roundService;
    private final LobbyService lobbyService;

    @Autowired
    public TurnService(TurnRepository turnRepository, GameService gameService, RoundService roundService, LobbyService lobbyService) {
        this.turnRepository = turnRepository;
        this.gameService = gameService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
    }

    @Transactional(readOnly = true)
    public List<Turn> findAllTurns() throws DataAccessException {
        return turnRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Turn> findTurnById(int id) throws DataAccessException {
        return turnRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Turn> findByRoundId(Integer id) throws DataAccessException {
        return turnRepository.findByRoundId(id);
    }

    @Transactional
    public void save(Turn turn) {
        turnRepository.save(turn);
    }

    @Transactional
     public void checkUserGame(User logedUser) {
        if (gameService.findGameByNickname(logedUser.getNickname()).isPresent()) {

            //TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que existe
            Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId()).get();
            Optional<Game> game = gameService.findGamebByLobbyId(lobby.getId());
            List<User> userList = lobby.getPlayerInternal();
            List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream().collect(Collectors.toList());
            if(roundList.size()!=0) {
                Round lastRound = roundList.get(roundList.size()-1);
                List<Turn> turnList = findByRoundId(lastRound.getId());
                Turn lastTurn = turnList.get(turnList.size()-1);
                if(lastTurn.getUser().getId()==logedUser.getId()) {
                    Turn turn = new Turn();
                    Integer nextUser = (userList.indexOf(logedUser)+1)%userList.size();
                    turn.setStartTime(LocalDateTime.now());
                    turn.setRound(lastRound);
                    turn.setUser(userList.get(nextUser));
                    save(turn);
                }
            }
        }
    }
}
