package sevenislands.achievement;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.register.RegisterService;
import sevenislands.user.User;

@Controller
public class AchievementController {

    private static final String VIEWS_ACHIEVEMENTS_LISTING = "achievements/achievementsListing";
    private static final String VIEWS_MY_ACHIEVEMENTS_LISTING = "achievements/myAchievementsListing";


    private final AchievementService achievementService;
    private final RegisterService registerService;

    @Autowired
    public AchievementController (AchievementService achievementService, RegisterService registerService) {
        this.achievementService = achievementService;
        this.registerService = registerService;
    }

    @GetMapping("/achievements")
    public String showAchievements(ModelMap model) {
        model.put("achievements", achievementService.findAchievements());
        return VIEWS_ACHIEVEMENTS_LISTING;
    }

    @GetMapping("/myAchievements")
    public String showMyAchievements(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Pair<Achievement, Date>> registers = registerService.findRegistersByNickname(logedUser.getNickname()).stream()
        .map(r -> Pair.of((Achievement)r[0], (Date)r[1])).collect(Collectors.toList());
        model.put("user", logedUser);
        model.put("achievements", registers);
        return VIEWS_MY_ACHIEVEMENTS_LISTING;
    }

}