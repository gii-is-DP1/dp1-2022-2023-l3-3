package sevenislands.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.user.User;

@Controller
public class StatisticController {

    private static final String VIEWS_MY_STATISTICS = "statistic/myStatistics";

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/myStatistics")
    public String showMyStatistics(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        model.put("user", logedUser);
        model.put("num_games_player", statisticService.numGamesNickname(logedUser.getNickname()));
        return VIEWS_MY_STATISTICS;
    }

}
