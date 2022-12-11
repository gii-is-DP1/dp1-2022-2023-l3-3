package sevenislands.game.turn;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.card.Card;
import sevenislands.card.CardService;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TurnController {

    private static final String VIEWS_GAME = "game/game"; // vista pasiva del juego

    private final TurnService turnService;
    private final UserService userService;
    private final RoundService roundService;
    private final LobbyService lobbyService;
    private final GameService gameService;
    private final IslandService islandService;
    private final CardService cardService;

    @Autowired
    public TurnController(GameService gameService, LobbyService lobbyService, RoundService roundService,
            TurnService turnService, IslandService islandService, UserService userService,CardService cardService) {
        this.turnService = turnService;
        this.userService = userService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.islandService = islandService;
        this.cardService=cardService;
    }

    @GetMapping("/turn")
    public String gameTurn(ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, NotExistLobbyException {
        if (userService.checkUserNoExists(request))
            return "redirect:/";
        if (lobbyService.checkUserNoLobby(logedUser))
            return "redirect:/home";
        response.addHeader("Refresh", "1");

        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
        List<Island> islandList = islandService.findIslandsByGameId(game.get().getId());
        if(turnService.endGame(game.get(),islandList)) return "game/endgame";
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                .collect(Collectors.toList());
        Round round = roundList.get(roundList.size() - 1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size() - 1);
        Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
        List<User> userList = lobby.getUsers();
        Map<Card, Integer> playerCardsMap = turnService.findPlayerCardsLastTurn(logedUser.getNickname());
        List<Island> islasToChose=turnService.IslandToChoose(lastTurn,logedUser.getNickname(),islandList);
        model.put("player", logedUser);
        model.put("player_turn", lastTurn.getUser());
        model.put("dice", lastTurn.getDice());
       model.put("IslasToChose", islasToChose);
       model.put("NumIslands", islandList.size()); 
       model.put("islandList", islandList);
        model.put("userList", userList);
        model.put("playerCardsMap", playerCardsMap);

        Duration timeElapsed = Duration.between(lastTurn.getStartTime(), LocalDateTime.now());
        model.put("time_left", 40 - timeElapsed.toSeconds());
        if (lastTurn.getUser().getId() == logedUser.getId() && timeElapsed.toSeconds() >= 40) {
            return "redirect:/turn/endTurn";
        }

        return VIEWS_GAME;
    }

    @GetMapping("/turn/endTurn")
    public String gameEndTurn(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request)
            throws ServletException {
        if (userService.checkUserNoExists(request))
            return "redirect:/";
        if (lobbyService.checkUserNoLobby(logedUser))
            return "redirect:/home";

        try {
            Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
            List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                    .collect(Collectors.toList());
            Round round = roundList.get(roundList.size() - 1);
            List<Turn> turnList = turnService.findByRoundId(round.getId());
            Turn lastTurn = turnList.get(turnList.size() - 1);
            Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
            List<User> userList = lobby.getUsers();

            if (logedUser.getId() == lastTurn.getUser().getId()) {
                if (turnList.size() >= userList.size())
                    return "redirect:/turn/newRound";
                turnService.initTurn(logedUser, round, userList, null);
            }
            return "redirect:/turn";
        } catch (Exception e) {
            return "redirect:/home";
        }
    }

    @GetMapping("/turn/dice")
    public String gameRollDice(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request)
            throws ServletException {
        if (userService.checkUserNoExists(request))
            return "redirect:/";
        if (lobbyService.checkUserNoLobby(logedUser))
            return "redirect:/home";

        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                .collect(Collectors.toList());
        Round round = roundList.get(roundList.size() - 1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size() - 1);
        turnService.dice(lastTurn);
        return "redirect:/turn";
    }

    @GetMapping("/turn/newRound")
    public String gameAsignTurn(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request)
            throws ServletException {
        if (userService.checkUserNoExists(request))
            return "redirect:/";
        if (lobbyService.checkUserNoLobby(logedUser))
            return "redirect:/home";
        try {
            Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
            if (game.isPresent()) {
                Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
                List<User> userList = lobby.getUsers();
                List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                        .collect(Collectors.toList());
                turnService.assignTurn(logedUser, game, userList, roundList);
                return "redirect:/turn";
            } else
            
                return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/home";
        }
    }

 
 
    @GetMapping("/turn/chooseIsland/{IdIsland}")
    public String chooseIsland(ModelMap model,@PathVariable("IdIsland") Integer id, @ModelAttribute("logedUser") User logedUser){
        Integer NumDelDado=turnService.findTurnByNickname(logedUser.getNickname()).get(0).getDice();
        Integer NumCartasEliminar=Math.abs(id-NumDelDado);
        return "redirect:/turn/chooseCard?islaId="+id+"&NumCartasDelete="+NumCartasEliminar;
    }

    @RequestMapping(value ="/turn/chooseCard",method = RequestMethod.GET)
        public String chooseCard(ModelMap model,@RequestParam Integer islaId,@RequestParam Integer NumCartasDelete, @ModelAttribute("logedUser") User logedUser){
        Card cardAnadida=cardService.findCardById(islaId);
        Optional<Game> game=gameService.findGameByNickname(logedUser.getNickname());
        Map<Card, Integer> playerCardsMap = turnService.findPlayerCardsLastTurn(logedUser.getNickname());
        if(NumCartasDelete.equals(0)){ 
            turnService.AnadirCarta(islaId,logedUser.getNickname());
            turnService.refreshDesk(islaId, logedUser, game);
            return "redirect:/turn/endTurn";
        }else{
            model.put("cardAnadida", cardAnadida);
            model.put("IslaId", islaId);
            model.put("cardsToEliminate", NumCartasDelete);
            model.put("card", playerCardsMap);
            return "/game/chooseCard";
        }
        
    
    }
        
        @RequestMapping(value="/delete/chooseCard/{idCard}",method = RequestMethod.GET)
        public String deleteMyCard(@PathVariable("idCard") Integer id,@RequestParam Integer islaId,@RequestParam Integer NumCartasDelete,@ModelAttribute("logedUser") User logedUser){
            turnService.DeleteCard(id, logedUser.getNickname());           
            NumCartasDelete--;         
            return "redirect:/turn/chooseCard?islaId="+islaId+"&NumCartasDelete="+NumCartasDelete;
        }
    
}
