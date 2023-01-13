package sevenislands.game;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.achievement.AchievementService;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.invitation.InvitationService;

@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VIEW_WELCOME = "redirect:/"; // vista para welcome
    private static final String VIEWS_FINISHED_GAMES = "list/finishedGames"; //vista de partidas finalizadas
    private static final String VIEWS_INPROGRESS_GAMES = "list/inProgressGames"; //vista de partidas en curso

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
    @MockBean
    private InvitationService invitationService;
    @MockBean
    private LobbyUserService lobbyUserService;
    @MockBean
    private DataSource dataSource;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
	private SessionRegistry sessionRegistry;
    @MockBean
	private AuthenticationManager authenticationManager;
    @MockBean
    private RoundService roundService;
    @MockBean
    private RegisterService registerService;

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
}
