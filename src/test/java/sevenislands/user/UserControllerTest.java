package sevenislands.user;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.LobbyService;
import sevenislands.register.RegisterService;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
	private MockMvc controller;

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
	private GameService gameService;

    @MockBean
    private RegisterService registerService;

    @MockBean
	private GameDetailsService gameDetailsService;

    @MockBean
	private TurnService turnService;

    private User userController;

    private static final String VIEWS_PLAYER_UPDATE_FORM = "views/updateUserForm";

    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
    }

    @WithMockUser(value = "spring")
    @Test
    public void testInitUpdateOwnerForm() throws Exception {
        
        given(userService.checkUser(any(), eq(userController))).willReturn(false);
        controller.perform(get("/settings", userController))
        .andExpect(status().is3xxRedirection());

    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerForm_validUser_loginSuccessful() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getSession()).willReturn(mock(HttpSession.class));
        given(request.getSession().getAttribute("logedUser")).willReturn(userController);
        BindingResult result = mock(BindingResult.class);
        given(result.hasErrors()).willReturn(false);
        given(userService.checkUser(any(), eq(userController))).willReturn(true);
    
        controller.perform(get("/settings").flashAttr("logedUser", userController))
        .andExpect(status().isOk()).andExpect(view().name(VIEWS_PLAYER_UPDATE_FORM));
    
    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerForm_whenFormHasErrors_thenReturnUpdateUserFormView() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("invalid-email");

        User logedUser = new User();
        logedUser.setId(1);
        logedUser.setPassword("password");

        when(userService.checkUser(any(), any())).thenThrow(new IllegalArgumentException());
        given(userService.save(any())).willThrow(new IllegalArgumentException());

        // Act and Assert
        controller.perform(post("/settings")
                .with(csrf())
                .flashAttr("logedUser", user))
                .andExpect(status().is3xxRedirection());
    }   
}

