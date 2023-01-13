package sevenislands.archievement;

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

import sevenislands.achievement.Achievement;
import sevenislands.achievement.AchievementController;
import sevenislands.achievement.AchievementService;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Optional;

@WebMvcTest(controllers = AchievementController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;
    @MockBean
    private AchievementService achievementService;
    @MockBean
    private UserService userService;

    private User userController;

    private static final String VIEWS_ACHIEVEMENTS_LISTING = "achievements/achievementsListing";
    private static final String VIEWS_MY_ACHIEVEMENTS_LISTING = "achievements/myAchievementsListing";

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
    public void showAchievementsTest() throws Exception {
        given(registerService.countAchievements()).willReturn(new ArrayList<>());
        
        mockMvc.perform(get("/achievements"))
        .andExpect(view().name(VIEWS_ACHIEVEMENTS_LISTING));
    }

    @WithMockUser(value = "spring")
    @Test
    public void showMyAchievementsTest() throws Exception {
        given(registerService.findRegistersByNickname(any())).willReturn(new ArrayList<>());
        
        mockMvc.perform(get("/myAchievements").flashAttr("logedUser", userController))
        .andExpect(view().name(VIEWS_MY_ACHIEVEMENTS_LISTING));
    }

    @WithMockUser(value = "spring")
    @Test
    public void controlAchievementsTest() throws Exception {
        given(achievementService.findAll()).willReturn(new ArrayList<>());
        
        mockMvc.perform(get("/controlAchievements").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name("achievements/controlAchievements"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void editAchievementsTest() throws Exception {
        given(achievementService.findAchievementById(any())).willReturn(Optional.empty());
        
        mockMvc.perform(get("/controlAchievements/edit/{idAchievement}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("achievements/controlAchievements"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void editAchievementsPresentTest() throws Exception {
        given(achievementService.findAchievementById(any())).willReturn(Optional.of(new Achievement()));
        
        mockMvc.perform(get("/controlAchievements/edit/{idAchievement}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("achievements/editControlAchievements"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void addAchievementTest() throws Exception {
        given(achievementService.findAll()).willReturn(new ArrayList<>());
        
        mockMvc.perform(get("/controlAchievements/add"))
        .andExpect(status().isOk())
        .andExpect(view().name("/achievements/addAchievement"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void processCreationAchievementFormTest() throws Exception {
        
        mockMvc.perform(post("/controlAchievements/add").flashAttr("achievement", new Achievement())
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/controlAchievements"));
    }
    
}
