package sevenislands.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.enums.Mode;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;

@Controller
public class StatisticController {

    private static final String VIEWS_STATISTICS = "statistic/statistics";
    private static final String VIEWS_DAILY_STATISTICS = "statistic/dailyStatistics";
    private static final String VIEWS_MY_STATISTICS = "statistic/myStatistics";

    private final GameService gameService;
    private final TurnService turnService;
    private final GameDetailsService gameDetailsService;
    private final LobbyUserService lobbyUserService;

    @Autowired
    public StatisticController(GameService gameService, TurnService turnService, 
    GameDetailsService gameDetailsService, LobbyUserService lobbyUserService) {
        this.gameService = gameService;
        this.turnService = turnService;
        this.gameDetailsService = gameDetailsService;
        this.lobbyUserService = lobbyUserService;
    }

    @GetMapping("/statistics")
    public String showStatistics(ModelMap model) {
        model.put("total_games", gameService.gameCount());
        model.put("total_time", gameService.findTotalTimePlayed());
        model.put("total_players", lobbyUserService.findTotalPlayersDistinctByMode(Mode.PLAYER));
        model.put("total_points", gameDetailsService.findTotalPunctuation());
        model.put("total_turns", turnService.turnCount());
        model.put("average_games", gameService.findAverageGamesPlayed());
        model.put("max_games", gameService.findMaxGamesPlayedADay());
        model.put("min_games", gameService.findMinGamesPlayedADay());
        model.put("average_time", gameService.findAverageTimePlayed());
        model.put("max_time", gameService.findMaxTimePlayed());
        model.put("min_time", gameService.findMinTimePlayed());
        model.put("average_players", gameService.findAveragePlayers());
        System.out.println("¿==================================");
        model.put("max_players", gameService.findMaxPlayers());
        System.out.println("?==================================");
        model.put("min_players", gameService.findMinPlayers());
        System.out.println("2b==================================");
        model.put("average_points", gameDetailsService.findAveragePunctuation());
        System.out.println("3==================================");
        model.put("max_points", gameDetailsService.findMaxPunctuation());
        model.put("min_points", gameDetailsService.findMinPunctuation());
        model.put("average_turns", turnService.findAverageTurns());
        model.put("max_turns", turnService.findMaxTurns());
        model.put("min_turns", turnService.findMinTurns());
        System.out.println("4==================================");
        return VIEWS_STATISTICS;
    }

    @GetMapping("/dailyStatistics")
    public String showDailyStatistics(ModelMap model) {
        model.put("total_games", gameService.gameCount());
        model.put("total_time", gameService.findTotalTimePlayed());
        model.put("total_players", lobbyUserService.findTotalPlayersDistinctByMode(Mode.PLAYER));
        model.put("total_points", gameDetailsService.findTotalPunctuation());
        model.put("total_turns", turnService.turnCount());
        model.put("average_games", gameService.findAverageGamesPlayed());
        model.put("max_games", gameService.findMaxGamesPlayedADay());
        model.put("min_games", gameService.findMinGamesPlayedADay());
        model.put("average_time_day", gameService.findDailyAverageTimePlayed());
        model.put("max_time_day", gameService.findMaxTimePlayedADay());
        model.put("min_time_day", gameService.findMinTimePlayedADay());
        model.put("average_players_day", gameService.findDailyAveragePlayers());
        model.put("max_players_day", gameService.findMaxPlayersADay());
        model.put("min_players_day", gameService.findMinPlayersADay());
        model.put("average_points_day", gameDetailsService.findDailyAveragePunctuation());
        model.put("max_points_day", gameDetailsService.findMaxPunctuationADay());
        model.put("min_points_day", gameDetailsService.findMinPunctuationADay());
        model.put("average_turns_day", turnService.findDailyAverageTurns());
        model.put("max_turns_day", turnService.findMaxTurnsADay());
        model.put("min_turns_day", turnService.findMinTurnsADay());
        return VIEWS_DAILY_STATISTICS;
    }

    @GetMapping("/myStatistics")
    public String showMyStatistics(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        model.put("user", logedUser);
        model.put("total_games_player", gameService.findTotalGamesPlayedByUser(logedUser));
        model.put("total_time_player", gameService.findTotalTimePlayedByUser(logedUser));
        model.put("total_points_player", gameDetailsService.findPunctuationByNickname(logedUser.getNickname()));
        model.put("total_turns_player", turnService.findTotalTurnsByNickname(logedUser.getNickname()));
        return VIEWS_MY_STATISTICS;
    }

}
