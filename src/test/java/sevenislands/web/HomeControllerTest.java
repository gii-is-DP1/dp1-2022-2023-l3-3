package sevenislands.web;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import javax.servlet.ServletException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import org.assertj.core.util.Lists;
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

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.lobby.Lobby;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.web.HomeController;

@WebMvcTest(value = HomeController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class HomeControllerTest {


    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;

    Game game;
    Lobby lobby;
    User logedUser;


    @BeforeEach
	void setup() throws NotExistLobbyException, ServletException {
		given(this.userService.checkUser(any(), any())).willReturn(true);
        logedUser = new User();
        logedUser.setNickname("userLoged");
        logedUser.setId(1);
        logedUser.setEmail("userLoged@gmail.com");
        logedUser.setEnabled(true);
	}

    @WithMockUser("spring")
	@Test
	public void home() throws Exception {
        mockMvc.perform(get("/home"))
        .andExpect(status().is3xxRedirection());
        given(this.userService.checkUser(any(), any())).willReturn(false);
        given(this.gameService.findGameByNickname(any())).willReturn(Optional.empty());
        
        mockMvc.perform(get("/home")
        .flashAttr("logedUser", logedUser))
        .andExpect(status().isOk())
        .andExpect(view().name("views/home"))
        .andExpect(model().attribute("hasPlayed", false))
        .andExpect(model().attribute("user", logedUser));
    
}
}
