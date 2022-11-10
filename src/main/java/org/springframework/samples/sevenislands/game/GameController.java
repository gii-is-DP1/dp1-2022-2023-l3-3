package org.springframework.samples.sevenislands.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

    private static final String VIEWS_GAME = "/games/game";

    private final GameService gameService;
    private final CardService cardService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public ModelAndView partida() {
        ModelAndView result = new ModelAndView(VIEWS_GAME);
        return result;
    }

}
