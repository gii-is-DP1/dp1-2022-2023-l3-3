package sevenislands.game;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.envers.tools.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.geo.GeoModule;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.achievement.AchievementService;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsController;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;
import sevenislands.user.UserService;

@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VIEWS_GAME_ASIGN_TURN = "game/asignTurn"; // vista para decidir turnos
    private static final String VIEW_WELCOME = "redirect:/"; // vista para welcome
    private static final String VIEWS_HOME =  "redirect:/home"; // vista para home
    private static final String VIEWS_TURN =  "redirect:/turn"; // vista para turn
    private static final String VIEWS_FINISHED_GAMES = "list/finishedGames"; //vista de partidas finalizadas
    private static final String VIEWS_INPROGRESS_GAMES = "list/inProgressGames"; //vista de partidas en curso
    private static final String VIEWS_GAMES_AS_PLAYER = "list/gamesAsPlayer"; //vista de partidas jugadas

    @MockBean
    private GameService gameService;
    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private UserService userService;
    @MockBean
    private TurnService turnService;
    @MockBean
    private GameDetailsService gameDetailsService;
    @MockBean
    private AchievementService achievementService;

    private User userController;
    private Game game;

    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
        userController.setEnabled(true);

        game = new Game();
        game.setActive(true);
    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameCheckUserNoExistTrueTest() throws Exception {
        
        given(userService.checkUserNoExists(any())).willReturn(true);
        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEW_WELCOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameCheckUserNoLobbyAndGameFalseTest() throws Exception {
        
        given(lobbyService.checkUserLobby(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);

        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_HOME));
    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameCheckAllPlayersTrueTest() throws Exception {
        given(lobbyService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyService.checkLobbyNoAllPlayers(any())).willReturn(true);

        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_HOME));
    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameCheckUserWithRoundTrueTest() throws Exception {
        
        given(lobbyService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkUserGameWithRounds(any())).willReturn(true);

        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_TURN));
    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameFinalTest() throws Exception {
        
        given(lobbyService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyService.findLobbyByPlayerId(any())).willReturn(new Lobby());

        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_GAME_ASIGN_TURN));
    }

    @WithMockUser(value = "spring")
    @Test
    public void createGameCathTest() throws Exception {
        
        given(lobbyService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyService.findLobbyByPlayerId(any())).willThrow(new NotExistLobbyException());

        mockMvc.perform(get("/game").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_HOME));
    }

    @WithMockUser(value = "spring")
    @Test
    public void endGameFalseTest() throws Exception {
        
        given(gameService.findGameByNickname(any())).willReturn(Optional.of(game));
        given(turnService.endGame(any())).willReturn(false);
        given(lobbyService.findLobbyByPlayerId(any())).willThrow(new NotExistLobbyException());

        mockMvc.perform(get("/endGame").flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/turn"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void endGameTest() throws Exception {
        game.setActive(false);
        given(gameService.findGameByNickname(any())).willReturn(Optional.of(game));
        List<Object[]> listaPair = new ArrayList<>();
        Object[] array = {userController, 2};
        listaPair.add(array);
        given(gameDetailsService.findPunctuationByGame(any())).willReturn(listaPair);

        mockMvc.perform(get("/endGame").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name("game/endgame"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void listEndGameTest() throws Exception {
        List<Object[]> listaPair = new ArrayList<>();
        Object[] array = {game, userController.getNickname()};
        listaPair.add(array);
        listaPair.add(array);
        given(gameService.findGameActive(any())).willReturn(listaPair);
        mockMvc.perform(get("/game/finished").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_FINISHED_GAMES));
    }

    @WithMockUser(value = "spring")
    @Test
    public void listInProgressGameTest() throws Exception {
        List<Object[]> listaPair = new ArrayList<>();
        Object[] array = {game, userController.getNickname()};
        listaPair.add(array);
        listaPair.add(array);
        given(gameService.findGameActive(any())).willReturn(listaPair);
    
        mockMvc.perform(get("/game/InProgress").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_INPROGRESS_GAMES));
    }

    @WithMockUser(value = "spring")
    @Test
    public void listGameAsPlayerTest() throws Exception {
        
        given(gameService.findGamesByNicknameAndActive(any(), any())).willReturn(Optional.of(new ArrayList<>()));
    
        mockMvc.perform(get("/game/gamesAsPlayer").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_GAMES_AS_PLAYER));
    }
    
}
