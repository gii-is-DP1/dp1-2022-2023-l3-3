package sevenislands.friends;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sevenislands.achievement.AchievementService;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.game.GameService;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.friend.Friend;
import sevenislands.user.friend.FriendController;
import sevenislands.user.friend.FriendService;
import sevenislands.user.invitation.InvitationService;

@WebMvcTest(controllers = FriendController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class FirendControllerTest {

    private final String VIEWS_FRIENDS_PAGE = "friend/friendsPage";
    private final String VIEWS_FRIENDS = "redirect:/friends";

    @Autowired
    private MockMvc mockMvc;

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
    private PasswordEncoder passwordEncoder;
    @MockBean
	private SessionRegistry sessionRegistry;
    @MockBean
	private AuthenticationManager authenticationManager;
    @MockBean
    private RoundService roundService;
    @MockBean
    private RegisterService registerService;
    @MockBean
    private FriendService friendService;


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
    public void getFriendsTest() throws Exception {

        
        
        mockMvc.perform(get("/friends").flashAttr("logedUser", userController))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_FRIENDS_PAGE));

    }

    @WithMockUser(value = "spring")
    @Test
    public void searchUserTest() throws Exception {

        given(userService.getCurrentUser()).willReturn(userController);
        given(userService.searchUserByNickname(any(), any())).willReturn(new ArrayList<>());
        mockMvc.perform(get( "/friends/search").flashAttr("searchedUser", userController).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_FRIENDS));

    }

    @WithMockUser(value = "spring")
    @Test
    public void addFriendTest() throws Exception {

        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(friendService.requestExists(any(), any())).willReturn(false);
        mockMvc.perform(get("/friends/add/{idUserSend}", userController.getId()).flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_FRIENDS));

    }

    @WithMockUser(value = "spring")
    @Test
    public void deleteFriendTest() throws Exception {

        given(friendService.deleteFriend(any(), any())).willReturn(Optional.of(new Friend()));
        mockMvc.perform(get("/friends/delete/{friendId}", userController.getId()).flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_FRIENDS));

    }

    @WithMockUser(value = "spring")
    @Test
    public void acceptFriendTest() throws Exception {

        given(friendService.acceptFriend(any(), any())).willReturn(Optional.of(new Friend()));
        mockMvc.perform(get("/friends/accept/{friendId}", userController.getId()).flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_FRIENDS));

    }
    
    @WithMockUser(value = "spring")
    @Test
    public void rejecttFriendTest() throws Exception {

        given(friendService.acceptFriend(any(), any())).willReturn(Optional.of(new Friend()));
        mockMvc.perform(get("/friends/reject/{friendId}", userController.getId()).flashAttr("logedUser", userController))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_FRIENDS));

    }
}
