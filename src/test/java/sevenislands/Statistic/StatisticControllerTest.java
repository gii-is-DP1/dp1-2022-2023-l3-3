package sevenislands.Statistic;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.achievement.AchievementService;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.game.Game;
import sevenislands.game.GameController;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.statistic.StatisticController;
import sevenislands.user.User;
import sevenislands.user.UserService;

@WebMvcTest(controllers = StatisticController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class StatisticControllerTest {

    private static final String VIEWS_STATISTICS = "statistic/statistics";
    private static final String VIEWS_DAILY_STATISTICS = "statistic/dailyStatistics";
    private static final String VIEWS_MY_STATISTICS = "statistic/myStatistics";

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GameService gameService;
    @MockBean
    private GameDetailsService gameDetailsService;
    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private LobbyUserService lobbyUserService;
    @MockBean
    private UserService userService;
    @MockBean
    private TurnService turnService;
    @MockBean
    private AchievementService achievementService;

    User userController;


    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
        userController.setEnabled(true);
    }


    @WithMockUser(value = "spring")
    @Test
    public void stadisticTest() throws Exception {
        
        given(gameService.gameCount()).willReturn(0L);
        given(gameService.findTotalTimePlayed()).willReturn(0L);
        given(gameDetailsService.findTotalPunctuation()).willReturn(0);
        given(turnService.turnCount()).willReturn(0);
        given(gameService.findAverageGamesPlayed()).willReturn(0.);
        given(gameService.findMaxGamesPlayedADay()).willReturn(0);
        given(gameService.findMinGamesPlayedADay()).willReturn(0);
        given(gameService.findAverageTimePlayed()).willReturn(0.);
        given(gameService.findMaxTimePlayed()).willReturn(0L);
        given(gameService.findMinTimePlayed()).willReturn(0L);
        given(gameDetailsService.findMaxPunctuation()).willReturn(0);
        given(gameDetailsService.findMinPunctuation()).willReturn(0);
        given(gameDetailsService.findAveragePunctuation()).willReturn(0.);
        given(turnService.findMaxTurns()).willReturn(0);
        given(turnService.findMinTurns()).willReturn(0);
        given(turnService.findAverageTurns()).willReturn(0.);

        mockMvc.perform(get("/statistics"))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_STATISTICS));

    }

    @WithMockUser(value = "spring")
    @Test
    public void dailyStadisticTest() throws Exception {
        
        given(gameService.gameCount()).willReturn(0L);
        given(gameService.findTotalTimePlayed()).willReturn(0L);
        given(gameDetailsService.findTotalPunctuation()).willReturn(0);
        given(turnService.turnCount()).willReturn(0);
        given(gameService.findAverageGamesPlayed()).willReturn(0.);
        given(gameService.findMaxGamesPlayedADay()).willReturn(0);
        given(gameService.findMinGamesPlayedADay()).willReturn(0);
        given(gameService.findDailyAverageTimePlayed()).willReturn(0.);
        given(gameService.findMaxTimePlayedADay()).willReturn(0L);
        given(gameService.findMinTimePlayedADay()).willReturn(0L);
        given(gameDetailsService.findMaxPunctuationADay()).willReturn(0);
        given(gameDetailsService.findMinPunctuationADay()).willReturn(0);
        given(gameDetailsService.findDailyAveragePunctuation()).willReturn(0.);
        given(turnService.findMaxTurnsADay()).willReturn(0);
        given(turnService.findMinTurnsADay()).willReturn(0);
        given(turnService.findDailyAverageTurns()).willReturn(0.);

        mockMvc.perform(get("/dailyStatistics"))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_DAILY_STATISTICS));

    }

    @WithMockUser(value = "spring")
    @Test
    public void myStadisticTest() throws Exception {
        
        given(gameService.findTotalGamesPlayedByUser(any())).willReturn(0);
        given(gameService.findTotalTimePlayedByUser(any())).willReturn(0L);
        given(gameDetailsService.findPunctuationByNickname(any())).willReturn(0L);
        given(turnService.findTotalTurnsByUser(any())).willReturn(0);   

        mockMvc.perform(get("/myStatistics").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_MY_STATISTICS));

    }

}
