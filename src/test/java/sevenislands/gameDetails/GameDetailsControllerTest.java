package sevenislands.gameDetails;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.game.GameService;
import sevenislands.user.User;
import sevenislands.user.UserService;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = GameDetailsController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameDetailsService gameDetailsService;
    @MockBean
    private GameService gameService;
    @MockBean
    private UserService userService;

    private User userController;


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
    public void rankingByPointsTest() throws Exception {

        given(gameDetailsService.findPunctuations()).willReturn(new ArrayList());
    
        mockMvc.perform(get("/ranking/points").flashAttr("logedUser", userController))
        .andExpect(status().isOk()).andExpect(view().name("views/rankingPoints"));
    
    }

    

    @WithMockUser(value = "spring")
    @Test
    public void rankingByVictoriesTest() throws Exception {

        given(gameDetailsService.findPunctuations()).willReturn(new ArrayList());
    
        mockMvc.perform(get("/ranking/victories").flashAttr("logedUser", userController))
        .andExpect(status().isOk()).andExpect(view().name("views/rankingVictories"));
    
    }
    
}
