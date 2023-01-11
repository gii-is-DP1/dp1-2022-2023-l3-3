package sevenislands.web;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import javax.servlet.ServletException;
import javax.sql.DataSource;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.round.RoundService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.invitation.InvitationService;
import sevenislands.web.HomeController;

// Test de HomeController con test para el método home usando la anotación @WebMvcTest con value, excludeFilters y excludeAutoConfiguration
@WebMvcTest(value = HomeController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class HomeControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private GameService gameService;

    @MockBean
    private InvitationService invitationService;

    @MockBean
    private LobbyUserService lobbyUserService;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
	private SessionRegistry sessionRegistry;

    @MockBean
	private LobbyService lobbyService;

    @MockBean
	private AuthenticationManager authenticationManager;

    @MockBean
    private RoundService roundService;

    @MockBean
    private RegisterService registerService;

    @MockBean
	private GameDetailsService gameDetailsService;

    Game game;
    Lobby lobby;
    User logedUser;


    @BeforeEach
	void setup() throws NotExistLobbyException, ServletException {
        game = new Game();
		given(this.invitationService.checkUser(any(), any())).willReturn(true);
        logedUser = new User();
        logedUser.setNickname("userLoged");
        logedUser.setId(1);
        logedUser.setEmail("userLoged@gmail.com");
        logedUser.setEnabled(true);
	}

    @Test
    @WithMockUser("spring")
	public void home() throws Exception {
        mockMvc.perform(get("/home"))
        .andExpect(status().is3xxRedirection());
        given(this.invitationService.checkUser(any(), any())).willReturn(false);
        given(this.invitationService.findInvitationsByReceiver(any())).willReturn(Lists.emptyList());
        given(this.gameService.findGameByUserAndMode(any(), any())).willReturn(Optional.empty());
        
        mockMvc.perform(get("/home")
        .flashAttr("logedUser", logedUser))
        .andExpect(status().isOk())
        .andExpect(view().name("views/home"))
        .andExpect(model().attribute("hasPlayed", false))
        .andExpect(model().attribute("user", logedUser))
        .andExpect(model().attribute("invitations", Lists.emptyList()));
    }
}
