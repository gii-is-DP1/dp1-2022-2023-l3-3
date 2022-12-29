package sevenislands.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.user.User;

@Controller
public class AchievementController {

    private static final String VIEWS_ACHIEVEMENTS_LISTING = "achievements/achievementsListing";
    private static final String VIEWS_MY_ACHIEVEMENTS_LISTING = "achievements/myAchievementsListing";


    private final AchievementService achievementService;

    @Autowired
    public AchievementController (AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping("/achievements")
    public String showAchievements(ModelMap model) {
        model.put("achievements", achievementService.findAchievements());
        return VIEWS_ACHIEVEMENTS_LISTING;
    }

    @GetMapping("/myAchievements")
    public String showMyAchievements(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        model.put("user", logedUser);
        model.put("achievements", achievementService.findAchievements());
        return VIEWS_MY_ACHIEVEMENTS_LISTING;
    }

}