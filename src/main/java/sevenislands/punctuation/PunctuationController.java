package sevenislands.punctuation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.user.User;

@Controller
public class PunctuationController {
    
    private final PunctuationService punctuationService;

    @Autowired
    public PunctuationController(PunctuationService punctuationService) {
        this.punctuationService = punctuationService;
    }

    @GetMapping("/ranking")
    public String showRanking(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Pair<User, Long>> rankings = punctuationService.findPunctuations().stream()
        .map(r -> Pair.of((User)r[0], (Long)r[1])).collect(Collectors.toList());

        model.put("rankings", rankings);
        return "views/ranking";
    }
}
