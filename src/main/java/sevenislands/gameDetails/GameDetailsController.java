package sevenislands.gameDetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.game.GameService;
import sevenislands.user.User;

@Controller
public class GameDetailsController {
    
    private final GameDetailsService gameDetailsService;
    private final GameService gameService;

    @Autowired
    public GameDetailsController(GameDetailsService gameDetailsService, GameService gameService) {
        this.gameDetailsService = gameDetailsService;
        this.gameService = gameService;
    }

    @GetMapping("/ranking/points")
    public String rankingByPoints(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Pair<User, Long>> rankings = gameDetailsService.findPunctuations().stream()
        .map(r -> Pair.of((User)r[0], (Long)r[1])).collect(Collectors.toList());
        model.put("rankings", rankings);
        return "views/rankingPoints";
    }

    @GetMapping("/ranking/victories")
    public String rankingByVictories(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Pair<User, Long>> rankings = gameService.findVictories().stream()
        .map(r -> Pair.of((User)r[0], (Long)r[1])).collect(Collectors.toList());

        model.put("rankings", rankings);
        return "views/rankingVictories";
    }
}
