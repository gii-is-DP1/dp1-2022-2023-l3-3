package sevenislands.game.turn;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.card.Card;
import sevenislands.card.CardService;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.message.Message;
import sevenislands.game.message.MessageService;
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
import org.springframework.web.bind.annotation.PostMapping;
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
    private final MessageService messageService;

    @Autowired
    public TurnController(GameService gameService, LobbyService lobbyService, RoundService roundService,
            TurnService turnService, IslandService islandService, UserService userService, CardService cardService,
            MessageService messageService) {
        this.turnService = turnService;
        this.userService = userService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.islandService = islandService;
        this.cardService = cardService;
        this.messageService = messageService;
    }

    @GetMapping("/turn")
    public String gameTurn(ModelMap model, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("message") String message) throws ServletException, NotExistLobbyException, Exception {
        if (userService.checkUserNoExists(request)) return "redirect:/";
        if(turnService.endGame(gameService.findGameByNickname(logedUser.getNickname()).get())) return "redirect:/endGame";
        if (!lobbyService.checkUserLobby(logedUser) && !gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(!gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(lobbyService.checkLobbyNoAllPlayers(logedUser)) return "redirect:/home";
        response.addHeader("Refresh", "10");
        
        Optional<Game> game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
        List<Island> islandList = islandService.findIslandsByGameId(game.get().getId());
        List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                .collect(Collectors.toList());
        Round round = roundList.get(roundList.size() - 1);
        List<Turn> turnList = turnService.findByRoundId(round.getId());
        Turn lastTurn = turnList.get(turnList.size() - 1);
        Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
        List<User> userList = lobby.getUsers();
        Map<Card, Integer> playerCardsMap = turnService.findPlayerCardsLastTurn(logedUser.getNickname());
        List<Island> islasToChose=turnService.islandToChoose(lastTurn,logedUser.getNickname(),islandList, request);
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
        HttpSession session = request.getSession();
        Map<Card,Integer> selectedCards = (Map<Card,Integer>) session.getAttribute("selectedCards");
        model.put("selectedCards",selectedCards);
        model.put("gameMessages", messageService.getMessages(game.get()));
        model.put("sentMessage", new Message());
        return VIEWS_GAME;
    }

    @PostMapping("/turn")
    public String sendMessage(@ModelAttribute("logedUser") User logedUser, @ModelAttribute("sentMessage") Message message,
            HttpServletRequest request) throws ServletException {
        if (userService.checkUserNoExists(request)) return "redirect:/";
        if (!lobbyService.checkUserLobby(logedUser) && !gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(!gameService.checkUserGame(logedUser)) return "redirect:/home";
        if (message != null) {
            Optional<Game> game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
            if(game.isPresent()) {
                messageService.saveMessage(logedUser, message.getMessage(), game.get());
            }
        }
        return "redirect:/turn";
    }

    @GetMapping("/turn/endTurn")
    public String gameEndTurn(@ModelAttribute("logedUser") User logedUser, HttpServletRequest request)
            throws ServletException {
        if(userService.checkUserNoExists(request)) return "redirect:/";
        if(!lobbyService.checkUserLobby(logedUser)  && !gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(!gameService.checkUserGame(logedUser)) return "redirect:/home";

        try {
            Optional<Game> game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
            List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                    .collect(Collectors.toList());
            Round round = roundList.get(roundList.size() - 1);
            List<Turn> turnList = turnService.findByRoundId(round.getId());
            Turn lastTurn = turnList.get(turnList.size() - 1);
            Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
            List<User> userList = lobby.getUsers();

            if (logedUser.getId() == lastTurn.getUser().getId()) {
                HttpSession session = request.getSession();
                Map<Card,Integer> selectedCards = new TreeMap<>();
                selectedCards = (Map<Card,Integer>) session.getAttribute("selectedCards");
                for(Card card : selectedCards.keySet()){
                    for(int i=0;i<selectedCards.get(card);i++) {
                        turnService.addCardToUser(card.getId(), logedUser);
                    }
                }
                session.setAttribute("selectedCards", new HashMap<>());
                
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
        if (userService.checkUserNoExists(request)) return "redirect:/";
        if (!lobbyService.checkUserLobby(logedUser)  && !gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(!gameService.checkUserGame(logedUser)) return "redirect:/home";

        Optional<Game> game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
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
        if (userService.checkUserNoExists(request)) return "redirect:/";
        if (!lobbyService.checkUserLobby(logedUser) && !gameService.checkUserGame(logedUser)) return "redirect:/home";
        if(!gameService.checkUserGame(logedUser)) return "redirect:/home";

        try {
            Optional<Game> game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
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
    public String chooseIsland(ModelMap model,@PathVariable("IdIsland") Integer id, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request){
        Optional<Game> game=gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
        turnService.addCarta(id,logedUser.getNickname());
        turnService.refreshDesk(id, logedUser, game);
        HttpSession session = request.getSession();
        Map<Card,Integer> selectedCards = new TreeMap<Card,Integer>();
        session.setAttribute("selectedCards", selectedCards);
        return "redirect:/turn/endTurn";
    }

    @RequestMapping(value="/turn/selectCard/{idCard}",method = RequestMethod.GET)
    public String selectCard(@PathVariable("idCard") Integer id, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request) {
        turnService.deleteCard(id, logedUser.getNickname());
        turnService.changeCard(id, logedUser, 0, request);
        return "redirect:/turn";
    }

    @RequestMapping(value="/turn/deselectCard/{idCard}",method = RequestMethod.GET)
    public String deselectCard(@PathVariable("idCard") Integer id, @ModelAttribute("logedUser") User logedUser, HttpServletRequest request) {
        turnService.addCardToUser(id, logedUser);
        turnService.changeCard(id, logedUser, 1, request);
        return "redirect:/turn";
    }
    
}
