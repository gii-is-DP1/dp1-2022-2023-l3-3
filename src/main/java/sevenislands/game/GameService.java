package sevenislands.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class GameService {

    private GameRepository gameRepository;
    private RoundService roundService;
    private final LobbyUserService lobbyUserService;
    private final LobbyService lobbyService;

    private final Integer minPlayers = 1;
    private final Integer maxPlayers = 4;

    @Autowired
    public GameService(RoundService roundService, GameRepository gameRepository, LobbyUserService lobbyUserService,
            LobbyService lobbyService) {
        this.gameRepository = gameRepository;
        this.roundService = roundService;
        this.lobbyUserService = lobbyUserService;
        this.lobbyService = lobbyService;
    }

    @Transactional
    public Long gameCount() {
        return gameRepository.count();
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

    // @Transactional
    // public Optional<List<Game>> findGamesByUserAndActive(User user, Boolean active) {
    //     List<Lobby> lobbies = lobbyUserService.findLobbiesByUser(user);
    //     Optional<List<Game>> gameList = gameRepository.findGameByLobbyAndActive(lobbies, active);
    //     if(gameList.isPresent()) {
    //          return gameList;
    //     }
    //     return Optional.empty();
    // }

    @Transactional
    public Optional<Game> findGameByUserAndActive(User user, Boolean active) {
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUser(user);
        Optional<List<Game>> gameList = gameRepository.findGameByLobbyAndActive(lobbies, active);
        if(gameList.isPresent()) {
            Optional<Game> result = Optional.of(gameList.get().get(0));
            return result;
        }
        return Optional.empty();
    }

    @Transactional
    public List<Object []> findGamePlayedByUser(User user) {
        List<Object []> result = new ArrayList<>();
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUserAndMode(user, Mode.PLAYER);
        List<Game> games = gameRepository.findGameActive(false).stream().filter(
            game -> lobbies.contains(game.getLobby())
        ).collect(Collectors.toList());
        games.stream().forEach(
            game -> {
                lobbyUserService.findUsersByLobby(game.getLobby()).stream().forEach(
                    userLobby -> {
                        result.add(new Object [] {game, userLobby.getNickname()});
                    }
                );
            }
        );
        return result;
    }

    @Transactional
    public List<Object []> findGameAndHostPLayedByUser(User user) {
        List<Object []> result = new ArrayList<>();
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUserAndMode(user, Mode.PLAYER);
        List<Game> games = gameRepository.findGameActive(false).stream().filter(
            game -> lobbies.contains(game.getLobby())
        ).collect(Collectors.toList());
        games.stream().forEach(
            game -> result.add(new Object [] {game, lobbyUserService.findUsersByLobby(game.getLobby()).get(0)})
        );
        return result;
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
        Optional<Game> game = findGameByUserAndActive(logedUser, true);
        return game.isPresent() && roundService.checkRoundByGame(game.get());
     }

    @Transactional
    public List<Object []> findGameActive(Boolean active) {
        List<Object []> result = new ArrayList<>();
        List<Game> games = gameRepository.findGameActive(active);
        games.stream().forEach(
            game -> {
                lobbyUserService.findUsersByLobby(game.getLobby()).stream().forEach(
                    user -> {
                        result.add(new Object [] {game, user.getNickname()});
                    }
                );
            }
        );
        return result;
    }

    @Transactional
    public Game endGame(User logedUser) {
        Optional<Game> game = findGameByUserAndActive(logedUser, true);
        if(game.isPresent()) {
            game.get().setActive(false);
            game.get().setEndingDate(LocalDateTime.now());
            return gameRepository.save(game.get());
        }
        return null;
    }

    @Transactional
    public Integer findTotalGamesPlayedByUser(User user) {
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUserAndMode(user, Mode.PLAYER);
        return gameRepository.findTotalGamesPlayedByUserLobbies(lobbies);
    }

    @Transactional
    public Optional<Game> findGameByLobby(Lobby lobby) {
        return gameRepository.findGameByLobby(lobby);
    }

    @Transactional
    public Optional<Game> findGameByUser(User user) throws NotExistLobbyException {
        try {
            Lobby lobby = lobbyUserService.findLobbyByUser(user);
            return gameRepository.findGameByLobby(lobby);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Game> findGameByUserAndMode(User user, Mode mode) throws NotExistLobbyException {
        try {
            Lobby lobby = lobbyUserService.findLobbyByUserAndMode(user, mode);
            return gameRepository.findGameByLobby(lobby);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Long findTotalTimePlayedByUser(User user) {
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUserAndMode(user, Mode.PLAYER);
        Optional<List<Game>> games = gameRepository.findGamesByLobbies(lobbies);
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
            if(g.getEndingDate()!=null) {
                LocalDateTime endingDate = g.getEndingDate();
                Duration diference = Duration.between(creationDate,endingDate);
                played = played.plus(diference);
            }
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

    public Boolean checkUserGame(User logedUser) throws NotExistLobbyException {
        Optional<Game> game = findGameByUser(logedUser);
        if(game.isPresent() && game.get().isActive()) return true;
        return false;
    }

    @Transactional
    public Long findVictoriesByNickname(String nickname) {
        return gameRepository.findVictoriesByNickname(nickname);
    }

    @Transactional
    public Double findAverageVictoriesPerGameByUser(User user) {
        Long totalVictories = gameRepository.findVictoriesByNickname(user.getNickname());
        Integer totalGames = findGamePlayedByUser(null).size();
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
        for(Game game : games) {
            if(game.getEndingDate()!=null) {
                LocalDateTime creationDate = game.getCreationDate();
                LocalDateTime endingDate = game.getEndingDate();
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
            if(endingDate!=null) {
                Duration diference = Duration.between(creationDate,endingDate);
                times.add(diference);
            }
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
    public Double findAveragePlayers() {
        Double average = (double) lobbyUserService.findTotalPlayersByMode(Mode.PLAYER) / gameCount();
        return Math.round(average * 100.0) / 100.0; 
    }

    @Transactional
    public Long findMaxPlayers() {
        List<Lobby> lobbies = gameRepository.findLobbies();
        Optional<List<Long>> totalPlayers = lobbyUserService.findTotalPlayersByLobbyAndMode(lobbies, Mode.PLAYER);
        if(totalPlayers.isPresent()) {
            Long maxPlayers = totalPlayers.get().stream().max(Comparator.naturalOrder()).get();
            return maxPlayers;
        }
        return 0L;
    }

    @Transactional
    public Long findMinPlayers() {
        List<Lobby> lobbies = gameRepository.findLobbies();
        Optional<List<Long>> totalPlayers = lobbyUserService.findTotalPlayersByLobbyAndMode(lobbies, Mode.PLAYER);
        if(totalPlayers.isPresent()) {
            return totalPlayers.get().stream().min(Comparator.naturalOrder()).get();
        }
        return 0L;
    }

    @Transactional
    public Double findDailyAveragePlayers() {
        Double average = (double) lobbyUserService.findTotalPlayersByMode(Mode.PLAYER) / findTotalGamesPlayedByDay().size();
        return Math.round(average * 100.0) / 100.0; 
    }

    @Transactional
    public Integer findMaxPlayersADay() {
        List<List<Lobby>> lobbies = gameRepository.findLobbiesByDay();
        List<Integer> playersByDay = lobbies.stream().map(lobbyList -> lobbyUserService.findTotalPlayersByDayAndMode(lobbyList, Mode.PLAYER)).collect(Collectors.toList());
        return playersByDay.stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPlayersADay() {
        List<List<Lobby>> lobbies = gameRepository.findLobbiesByDay();
        List<Integer> playersByDay = lobbies.stream().map(lobbyList -> lobbyUserService.findTotalPlayersByDayAndMode(lobbyList, Mode.PLAYER)).collect(Collectors.toList());
        return playersByDay.stream().min(Comparator.naturalOrder()).get();
    }
    
    @Transactional
    public Lobby leaveLobby(User user) throws NotExistLobbyException {
        Lobby lobby = lobbyUserService.findLobbyByUser(user);
        Optional<Game> game = findGameByLobby(lobby);
		if(lobby.isActive() || (game.isPresent() && game.get().isActive())) {
            List<User> users = lobbyUserService.findUsersByLobby(lobby);
            if (users.size() == minPlayers) {
                lobby.setActive(false);
            }
            Optional<LobbyUser> lobbyUser = lobbyUserService.findByLobbyAndUser(lobby, user);
            if(lobbyUser.isPresent()) {
                lobbyUserService.deleteLobbyUser(lobbyUser.get());
                lobby = lobbyService.save(lobby);
            }
        } 
        return lobby;
    }

    @Transactional
    public List<String> checkLobbyErrors(String code) throws NotExistLobbyException {
        List<String> errors = new ArrayList<>();
        try {
            code = code.trim();
            Lobby lobby = lobbyService.findLobbyByCode(code);
            Integer userNumber = lobbyUserService.findUsersByLobby(lobby).size();
		    if(!lobby.isActive()) errors.add("La partida ya ha empezado o ha finalizado");
		    if(userNumber == maxPlayers) errors.add("La lobby está llena");
            return errors;
        } catch (Exception e) {
            errors.add("El código no pertenece a ninguna lobby");
            return errors;
        }
    }

    @Transactional
    public Boolean checkLobbyNoAllPlayers(User logedUser) throws Exception {
        try {
            Integer userNumber = null;
            Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
            Boolean res;
            userNumber = lobbyUserService.findUsersByLobbyAndMode(lobby, Mode.PLAYER).size();
		if (userNumber > minPlayers && userNumber <= maxPlayers) {
            res = false;
		} else {res= true;}
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Boolean ejectPlayer(User logedUser, User ejectedUser) throws Exception {
		try {
            Lobby lobbyEjectedUser = lobbyUserService.findLobbyByUser(ejectedUser);
            Lobby lobbyLogedUser = lobbyUserService.findLobbyByUser(logedUser);
            Boolean res;
            if(lobbyEjectedUser.equals(lobbyLogedUser)) {
                if (ejectedUser.getNickname().equals(logedUser.getNickname())) {
                    leaveLobby(logedUser);
                    res = false;
                } else {
                    Optional<LobbyUser> lobbyUser = lobbyUserService.findByLobbyAndUser(lobbyLogedUser, ejectedUser);
                    if(lobbyUser.isPresent()) {
                        lobbyUserService.deleteLobbyUser(lobbyUser.get());
                        res = true;
                    } else res = false;
                }
            } else res = true;
                return res;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Boolean checkUserViewer(User logedUser) {
        try {
            Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
            Optional<Game> game = gameRepository.findGameByLobby(lobby);
            Optional<LobbyUser> lobbyUser = lobbyUserService.findByLobbyAndUser(lobby, logedUser);
            if(lobbyUser.isPresent() && game.isPresent() && game.get().isActive()) {
                return lobbyUser.get().getMode().equals(Mode.VIEWER);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional
    public Double findAverageGamePlayedByUserPerDay(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
        if(games.isPresent()) {
            List<Integer> gamesPerDay = gameRepository.findNumberGamesByGameListPerDay(games.get());
            Integer total = gamesPerDay.stream().reduce(0, (a,b) -> a + b);
            return (double) total / gamesPerDay.size();
        }
        return 0.0;
    }

    @Transactional
    public Integer findMaxGamePlayedByUserPerDay(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
        if(games.isPresent()) {
            List<Integer> gamesPerDay = gameRepository.findNumberGamesByGameListPerDay(games.get());
            Integer maxPlayed = gamesPerDay.stream().max(Integer::compareTo).get();
            return maxPlayed;
        }
        return 0;
    }

    @Transactional
    public Integer findMinGamePlayedByUserPerDay(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
        if(games.isPresent()) {
            List<Integer> gamesPerDay = gameRepository.findNumberGamesByGameListPerDay(games.get());
            Integer minPlayed = gamesPerDay.stream().min(Integer::compareTo).get();
            return minPlayed;
        }
        return 0;

    }

    @Transactional
    public Double findAverageTimePlayedByUserPerDay(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
        if(games.isPresent()) {
            Long totalTime = findTotalTimePlayedByUser(user);
            List<Integer> gamesPerDay = gameRepository.findNumberGamesByGameListPerDay(games.get());
            Integer totalDays = gamesPerDay.size();
            return (double) totalTime / totalDays;
        }
        return 0.0;
    }

    @Transactional
    public Optional<List<Game>> findGamesByUser(User user) {
        List<Lobby> lobbies = lobbyUserService.findLobbiesByUserAndMode(user, Mode.PLAYER);
        List<Game> games = gameRepository.findGameActive(false);
        if(games.isEmpty()) return Optional.empty();
        games = games.stream().filter(
            game -> lobbies.contains(game.getLobby())
        ).collect(Collectors.toList());
        return Optional.of(games);
    }

    @Transactional
    public Double findMaxTimePlayedByUser(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
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
    public Double findMinTimePlayedByUser(User user) {
        Optional<List<Game>> games = findGamesByUser(user);
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
