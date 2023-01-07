package sevenislands.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.user.User;

@Controller
public class StatisticController {

    private static final String VIEWS_STATISTICS = "statistic/statistics";
    private static final String VIEWS_MY_STATISTICS = "statistic/myStatistics";

    private final GameService gameService;
    private final TurnService turnService;
    private final GameDetailsService gameDetailsService;

    @Autowired
    public StatisticController(GameService gameService, TurnService turnService, GameDetailsService gameDetailsService) {
        this.gameService = gameService;
        this.turnService = turnService;
        this.gameDetailsService = gameDetailsService;
    }

    @GetMapping("/statistics")
    public String showStatistics(ModelMap model) {
        model.put("total_games", gameService.gameCount());
        model.put("total_time", gameService.findTotalTimePlayed());
        model.put("total_points", gameDetailsService.findTotalPunctuation());
        model.put("total_turns", turnService.turnCount());
        model.put("average_games", gameService.findAverageGamesPlayed());
        model.put("max_games", gameService.findMaxGamesPlayedADay());
        model.put("min_games", gameService.findMinGamesPlayedADay());
        model.put("average_time", gameService.findAverageTimePlayed());
        model.put("max_time", gameService.findMaxTimePlayedADay());
        model.put("min_time", gameService.findMinTimePlayedADay());
        model.put("average_points", gameDetailsService.findAveragePunctuation());
        model.put("max_points", gameDetailsService.findMaxPunctuationADay());
        model.put("min_points", gameDetailsService.findMinPunctuationADay());
        return VIEWS_STATISTICS;
    }

    @GetMapping("/myStatistics")
    public String showMyStatistics(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        model.put("user", logedUser);
        model.put("total_games_player", gameService.findTotalGamesPlayedByNickname(logedUser.getNickname()));
        model.put("total_time_player", gameService.findTotalTimePlayedByNickname(logedUser.getNickname()));
        model.put("total_points_player", gameDetailsService.findPunctuationByNickname(logedUser.getNickname()));
        model.put("total_turns_player", turnService.findTotalTurnsByNickname(logedUser.getNickname()));
        return VIEWS_MY_STATISTICS;
    }

}
