package sevenislands.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.LocalDate;
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
    public List<Game> findAll() {
        return gameRepository.findAll();
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

    @Transactional
    public List<Object []> findGamePLayedByNickname(String nickname) {
        return gameRepository.findGamePLayedByNickname(nickname);
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
    public List<Object []> findGameActive(Boolean active) {
        return gameRepository.findGameActive(active);
    }

    @Transactional
    public Optional<Game> endGame(User logedUser) {
        Optional<Game> game = findGameByNicknameAndActive(logedUser.getNickname(), true);
        if(game.isPresent()) {
            game.get().setActive(false);
            game.get().setEndingDate(LocalDateTime.now());
            gameRepository.save(game.get());
        }
        return game;
    }

    @Transactional
    public Integer findTotalGamesPlayedByNickname(String nickname) {
        return gameRepository.findTotalGamesPlayedByNickname(nickname);
    }

    @Transactional
    public Long findTotalTimePlayedByNickname(String nickname) {
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

    @Transactional
    public Long findTotalTimePlayed() {
        List<Game> games = findAll();
        Duration played = Duration.ZERO;
        for(Game g : games) {
            LocalDateTime creationDate = g.getCreationDate();
            LocalDateTime endingDate = g.getEndingDate();
            Duration diference = Duration.between(creationDate,endingDate);
            played = played.plus(diference);
        }

        return played.toMinutes();   
    }

    @Transactional
    public List<Integer> findTotalGamesPlayedByDay() {
        return gameRepository.findTotalGamesPlayedByDay();
    }

    @Transactional
    public Double findAverageGamesPlayed() {
        Double average = (double) gameCount() / findTotalGamesPlayedByDay().size();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Integer findMaxGamesPlayedADay() {
        return findTotalGamesPlayedByDay().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinGamesPlayedADay() {
        return findTotalGamesPlayedByDay().stream().min(Comparator.naturalOrder()).get();
    }

    public Boolean checkUserGame(User logedUser) {
        Optional<Game> game = findGameByNickname(logedUser.getNickname());
        if(game.isPresent() && game.get().isActive()) return true;
        return false;
    }

    @Transactional
    public Long findVictoriesByNickname(String nickname) {
        return gameRepository.findVictoriesByNickname(nickname);
    }

    @Transactional
    public Double findAverageVictoriesPerGameByNickname(String nickname) {
        Long totalVictories = gameRepository.findVictoriesByNickname(nickname);
        Integer totalGames = gameRepository.findTotalGamesPlayedByNickname(nickname);
        Double averageVictories = 0.;
        if(totalVictories != null && totalGames != null) averageVictories = (double) totalVictories / totalGames;
        return averageVictories;
    }

    @Transactional
    public Integer findMaxVictoriesPerDayByNickname(String nickname) {
        List<Game> wonGames = gameRepository.findWonGamesByNickname(nickname);
        Integer maxWonGamesPerDay = 0;
        if(!wonGames.isEmpty()) {
            Map<LocalDate, List<Game>> wonGamesByDay = wonGames.stream()
            .collect(Collectors.groupingBy(g -> g.getCreationDate().toLocalDate()));

            maxWonGamesPerDay = wonGamesByDay.values().stream()
            .map(wonGamesList -> wonGamesList.size())
            .reduce(0, Integer::max);
        }
        return maxWonGamesPerDay;
    }

    @Transactional
    public Integer findMinVictoriesPerDayByNickname(String nickname) {
        List<Game> wonGames = gameRepository.findWonGamesByNickname(nickname);
        Integer minWonGamesPerDay = 0;
        if(!wonGames.isEmpty()) {
            Map<LocalDate, List<Game>> wonGamesByDay = wonGames.stream()
            .collect(Collectors.groupingBy(g -> g.getCreationDate().toLocalDate()));

            minWonGamesPerDay = wonGamesByDay.values().stream()
            .map(wonGamesList -> wonGamesList.size())
            .min(Integer::compareTo)
            .get();
        }
        return minWonGamesPerDay;
    }

    @Transactional
    public List<Object []> findVictories() {
        return gameRepository.findVictories();
    }

    @Transactional
    public Long findTieBreaksByNickname(String nickname) {
        return gameRepository.findTieBreaksByNickname(nickname);
    }

    @Transactional
    public Double findDailyAverageTimePlayed() {
        Double average = (double) findTotalTimePlayed() / findTotalGamesPlayedByDay().size();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Map<LocalDate, Duration> findGamesDurationsByDate() {
        Map<LocalDate, Duration> durationsByDate = new HashMap<>();
        List<Game> games = findAll();
        for(Game g : games) {
            LocalDateTime creationDate = g.getCreationDate();
            LocalDateTime endingDate = g.getEndingDate();
            LocalDate date = creationDate.toLocalDate();
            Duration diference = Duration.between(creationDate,endingDate);
            if(durationsByDate.containsKey(date)) {
                Duration updatedDuration = durationsByDate.get(date).plus(diference);
                durationsByDate.put(date, updatedDuration);
            }
            else {
                durationsByDate.put(date, diference);
            }
        }
        return durationsByDate;
    }

    @Transactional
    public Long findMaxTimePlayedADay() {
        Map<LocalDate, Duration> gamesDurationsByDate = findGamesDurationsByDate();
        Duration maxDuration = null;
        Optional<Map.Entry<LocalDate, Duration>> maxEntry = gamesDurationsByDate.entrySet().stream()
        .max(Comparator.comparing(Map.Entry::getValue));
        if(maxEntry.isPresent()) {
            maxDuration = maxEntry.get().getValue();
        }
        return maxDuration != null ? maxDuration.toMinutes() : null;
    }

    @Transactional
    public Long findMinTimePlayedADay() {
        Map<LocalDate, Duration> gamesDurationsByDate = findGamesDurationsByDate();
        Duration minDuration = null;
        Optional<Map.Entry<LocalDate, Duration>> minEntry = gamesDurationsByDate.entrySet().stream()
        .min(Comparator.comparing(Map.Entry::getValue));
        if(minEntry.isPresent()) {
            minDuration = minEntry.get().getValue();
        }
        return minDuration != null ? minDuration.toMinutes() : null;
    }

    @Transactional
    public Double findAverageTimePlayed() {
        Double average = (double) findTotalTimePlayed() / gameCount();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public List<Duration> findTotalTimePlayedByGame() {
        List<Game> games = findAll();
        List<Duration> times = new ArrayList<>();
        for(Game g : games) {
            LocalDateTime creationDate = g.getCreationDate();
            LocalDateTime endingDate = g.getEndingDate();
            Duration diference = Duration.between(creationDate,endingDate);
            times.add(diference);
        }
        return times;
    }

    @Transactional
    public Long findMaxTimePlayed() {
        return findTotalTimePlayedByGame().stream().max(Comparator.naturalOrder()).get().toMinutes();
    }

    @Transactional
    public Long findMinTimePlayed() {
        return findTotalTimePlayedByGame().stream().min(Comparator.naturalOrder()).get().toMinutes();
    }

    @Transactional
    public Integer findTotalPlayersDistinct() {
        return gameRepository.findTotalPlayersDistinct();
    }

    @Transactional
    public Double findAveragePlayers() {
        Double average = (double) gameRepository.findTotalPlayers() / gameCount();
        return Math.round(average * 100.0) / 100.0; 
    }

    @Transactional
    public Integer findMaxPlayers() {
        return gameRepository.findTotalPlayersByGame().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPlayers() {
        return gameRepository.findTotalPlayersByGame().stream().min(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Double findDailyAveragePlayers() {
        Double average = (double) gameRepository.findTotalPlayers() / findTotalGamesPlayedByDay().size();
        return Math.round(average * 100.0) / 100.0; 
    }

    @Transactional
    public Integer findMaxPlayersADay() {
        return gameRepository.findTotalPlayersByDay().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPlayersADay() {
        return gameRepository.findTotalPlayersByDay().stream().min(Comparator.naturalOrder()).get();
    }
    
    @Transactional
    public Double findAverageGamePlayedByNicknamePerDay(String nickname) {
        List<Integer> gamesPerDay = gameRepository.findNGamesPlayedByNicknamePerDay(nickname);
        Integer total = gamesPerDay.stream().reduce(0, (a,b) -> a + b);
        return (double) total / gamesPerDay.size();
    }

    @Transactional
    public Integer findMaxGamePlayedByNicknamePerDay(String nickname) {
        List<Integer> gamesPerDay = gameRepository.findNGamesPlayedByNicknamePerDay(nickname);
        Integer maxPlayed = gamesPerDay.stream().max(Integer::compareTo).get();
        return maxPlayed;
    }

    @Transactional
    public Integer findMinGamePlayedByNicknamePerDay(String nickname) {
        List<Integer> gamesPerDay = gameRepository.findNGamesPlayedByNicknamePerDay(nickname);
        Integer minPlayed = gamesPerDay.stream().min(Integer::compareTo).get();
        return minPlayed;
    }

    @Transactional
    public Double findAverageTimePlayedByNicknamePerDay(String nickname) {
        Long totalTime = findTotalTimePlayedByNickname(nickname);
        List<Integer> gamesPerDay = gameRepository.findNGamesPlayedByNicknamePerDay(nickname);
        Integer totalDays = gamesPerDay.size();
        return (double) totalTime / totalDays;
    }

    @Transactional
    public Double findMaxTimePlayedByNickname(String nickname) {
        Optional<List<Game>> games = gameRepository.findGameByNickname(nickname);
        Long maxTime = 0L;
        if(games.isPresent()) {
            maxTime = games.get().stream()
            .map(g -> Duration.between(g.getCreationDate(), g.getEndingDate()))
            .map(d -> d.toMinutes())
            .max(Long::compareTo)
            .get();
        }
        return (double) maxTime;
    }

    @Transactional
    public Double findMinTimePlayedByNickname(String nickname) {
        Optional<List<Game>> games = gameRepository.findGameByNickname(nickname);
        Long minTime = 0L;
        if(games.isPresent()) {
            minTime = games.get().stream()
            .map(g -> Duration.between(g.getCreationDate(), g.getEndingDate()))
            .map(d -> d.toMinutes())
            .min(Long::compareTo)
            .get();
        }
        return (double) minTime;
    }
}
