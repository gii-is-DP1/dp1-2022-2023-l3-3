package sevenislands.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import sevenislands.achievement.AchievementService;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class GameController {

    private static final String VIEWS_GAME_ASIGN_TURN = "game/asignTurn"; // vista para decidir turnos
    private static final String VIEW_WELCOME = "redirect:/"; // vista para welcome
    private static final String VIEWS_HOME =  "redirect:/home"; // vista para home
    private static final String VIEWS_TURN =  "redirect:/turn"; // vista para turn
    private static final String VIEWS_FINISHED_GAMES = "list/finishedGames"; //vista de partidas finalizadas
    private static final String VIEWS_INPROGRESS_GAMES = "list/inProgressGames"; //vista de partidas en curso
    private static final String VIEWS_GAMES_AS_PLAYER = "list/gamesAsPlayer"; //vista de partidas jugadas

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;
    private final TurnService turnService;
    private final GameDetailsService gameDetailsService;
    private final AchievementService achievementService;
    private final LobbyUserService lobbyUserService;

    @Autowired
    public GameController(UserService userService, GameService gameService, LobbyService lobbyService, 
    TurnService turnService, GameDetailsService gameDetailsService, AchievementService achievementService,
    LobbyUserService lobbyUserService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.turnService = turnService;
        this.gameDetailsService = gameDetailsService;
        this.achievementService = achievementService;
        this.lobbyUserService = lobbyUserService;
    }

    @GetMapping("/game")
    public String createGame(HttpServletRequest request, @ModelAttribute("logedUser") User logedUser, HttpServletResponse response) throws Exception {
        if(userService.checkUserNoExists(request)) return VIEW_WELCOME;
        if(!lobbyUserService.checkUserLobby(logedUser) && !gameService.checkUserGame(logedUser)) return VIEWS_HOME;
        if(gameService.checkLobbyNoAllPlayers(logedUser)) return "redirect:/home";
        if(gameService.checkUserGameWithRounds(logedUser)) return VIEWS_TURN;
        response.addHeader("Refresh", "5");
        try {
        Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
        if(gameService.findGameByUserAndActive(logedUser, true).isEmpty()) {
            gameService.initGame(lobby);
            lobbyService.disableLobby(lobby);
        }
        return VIEWS_GAME_ASIGN_TURN;
       } catch (Exception e) {
        return VIEWS_HOME;
       }
    }

    @GetMapping("/endGame")
    public String endGame(ModelMap model, @ModelAttribute("logedUser") User logedUser) throws NotExistLobbyException{
        Optional<Game> game = gameService.findGameByUser(logedUser);
        if(game.isPresent() && game.get().isActive()) {
            if(!turnService.endGame(game.get())) return "redirect:/turn";
            if(game.get().getEndingDate()==null) gameService.endGame(logedUser);
            gameDetailsService.calculateDetails(logedUser);
            achievementService.calculateAchievements(logedUser);
        }
        
        List<Pair<User, Integer>> players = gameDetailsService.findPunctuationByGame(game.get()).stream()
        .map(r -> Pair.of((User)r[0], (Integer)r[1])).collect(Collectors.toList());

        model.put("logedUser", logedUser);
        model.put("winner", players.get(0).getFirst());
        model.put("players", players);
        
        return"game/endgame";
    }

    @GetMapping("/game/finished")
    public String listFinishedGames(ModelMap model) {
        HashMap<Game, String[]> gamesResult = new HashMap<>(); 
        List<Object[]> games = this.gameService.findGameActive(false);

        games.stream().forEach(g -> {
            if (gamesResult.containsKey(g[0])) {
                Game game = (Game) g[0];
                String[] list = gamesResult.get(game);
                String[] both = Stream.of(list, new String[] {(String) g[1]}).flatMap(Stream::of)
                      .toArray(String[]::new);
                
                gamesResult.put(game, both);
            } else {
                gamesResult.put((Game) g[0], new String[] {(String) g[1]});
            }
        });
        model.put("games", gamesResult.entrySet().stream().map(r -> Pair.of((Game)r.getKey(), (String[])r.getValue())).collect(Collectors.toList()));
        return VIEWS_FINISHED_GAMES;
    }

    @GetMapping("/game/InProgress")
    public String listGames(ModelMap model) {
        HashMap<Game, String[]> gamesResult = new HashMap<>(); 
        List<Object[]> games = this.gameService.findGameActive(true);

        games.stream().forEach(g -> {
            if (gamesResult.containsKey(g[0])) {
                Game game = (Game) g[0];
                String[] list = gamesResult.get(game);
                String[] both = Stream.of(list, new String[] {(String) g[1]}).flatMap(Stream::of)
                      .toArray(String[]::new);
                
                gamesResult.put(game, both);
            } else {
                gamesResult.put((Game) g[0], new String[] {(String) g[1]});
            }
        });
        model.put("games", gamesResult.entrySet().stream().map(r -> Pair.of((Game)r.getKey(), (String[])r.getValue())).collect(Collectors.toList()));
        return VIEWS_INPROGRESS_GAMES;
    }

    @GetMapping("/game/gamesAsPlayer")
    public String listGamesAsPlayer(ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        List<Object []> games = gameService.findGameAndHostPLayedByUser(logedUser);
        List<Pair<Game, User>> gamesResult = games.stream().map(r -> Pair.of((Game)r[0], (User)r[1])).collect(Collectors.toList());
        model.put("games", gamesResult);
        return VIEWS_GAMES_AS_PLAYER;
    }
}
