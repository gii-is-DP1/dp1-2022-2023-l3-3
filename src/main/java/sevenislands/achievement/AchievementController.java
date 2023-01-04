package sevenislands.achievement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sevenislands.enums.AchievementType;
import sevenislands.register.RegisterService;
import sevenislands.user.User;

@Controller
public class AchievementController {

    private static final String VIEWS_ACHIEVEMENTS_LISTING = "achievements/achievementsListing";
    private static final String VIEWS_MY_ACHIEVEMENTS_LISTING = "achievements/myAchievementsListing";

    private final RegisterService registerService;
    private final AchievementService achievementService;

    @Autowired
    public AchievementController (RegisterService registerService,AchievementService achievementService) {
        this.registerService = registerService;
        this.achievementService = achievementService;
    }

    @GetMapping("/achievements")
    public String showAchievements(ModelMap model) {
        List<Pair<Achievement, Long>> achievements = registerService.countAchievements().stream()
        .map(r -> Pair.of((Achievement)r[0], (Long)r[1])).collect(Collectors.toList());

        model.put("achievements", achievements);
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

    @GetMapping("/controlAchievements")
    public String controlAchievements(ModelMap model, @ModelAttribute("logedUser")User logedUser){
        List<Pair<Achievement, Integer>> achievements = achievementService.findAll().stream()
        .map(r -> Pair.of((Achievement)r ,(Integer)r.getThreshold())).collect(Collectors.toList());
        model.put("logros", achievements);
        return "achievements/controlAchievements";
    }

    @GetMapping("/controlAchievements/edit/{idAchievement}")
    public String editAchievements(ModelMap model,@PathVariable("idAchievement") Integer id){
        Optional<Achievement> editAchievement=achievementService.findAchievementById(id);
        if(editAchievement.isPresent()){
        model.put("tipoLogro",List.of(AchievementType.values()));
        model.put("achievement", editAchievement.get());
        model.put("idAchievement", id);
        return "achievements/editControlAchievements";
    }else{
        return "achievements/controlAchievements";
    }
    }

    @PostMapping("/controlAchievements/edit/{idAchievement}")
    public String processEditAchievementForm(ModelMap model, @PathVariable("idAchievement") Integer id, @Valid Achievement achievement, BindingResult result) {
        if(result.hasErrors()){
            return "/home";
        }
        try{
            achievementService.updateAchievement(achievement,id.toString());
        }catch(Exception e){
           throw e;
        }
        return "redirect:/controlAchievements";
    }


    @GetMapping("/controlAchievements/add")
    public String addAchievement(ModelMap model){
        model.put("achievement", new Achievement());
        model.put("tipoLogro", List.of(AchievementType.values()));
        return "/achievements/addAchievement";
    }

    @PostMapping("/controlAchievements/add")
    public String processCreationAchievementForm(ModelMap model,@Valid Achievement achievement,BindingResult result){
        if(result.hasErrors()) return "/achievements/addAchievement";
        try {
            achievementService.addAchievement(achievement);
        } catch (Exception e) {
            throw e;
        }
        return "redirect:/controlAchievements";
    }

}