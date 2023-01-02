package sevenislands.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.user.User;

@Controller
public class StatisticController {

    private static final String VIEWS_MY_STATISTICS = "statistic/myStatistics";

    private final GameService gameService;
    private final TurnService turnService;

    @Autowired
    public StatisticController(GameService gameService, TurnService turnService) {
        this.gameService = gameService;
        this.turnService = turnService;
    }

    @GetMapping("/myStatistics")
    public String showMyStatistics(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        model.put("user", logedUser);
        model.put("num_games_player", gameService.findTotalGamesPlayedByNickname(logedUser.getNickname()));
        model.put("num_turns_player", turnService.findTotalTurnsByNickname(logedUser.getNickname()));
        model.put("total_time_played", gameService.findTotalTimePlayed(logedUser.getNickname()));
        return VIEWS_MY_STATISTICS;
    }

}
