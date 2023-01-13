package sevenislands.lobby;

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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.Mode;
import sevenislands.enums.UserType;
import sevenislands.game.GameService;
import sevenislands.game.island.IslandService;
import sevenislands.game.message.MessageService;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;
import sevenislands.user.friend.FriendService;
import sevenislands.user.invitation.Invitation;
import sevenislands.user.invitation.InvitationService;

@WebMvcTest(controllers = LobbyController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class LobbyControllerTest {

    @Autowired
	private MockMvc mockMvc;

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
    @MockBean
	private RoundService roundService;
    @MockBean
	private IslandService islandService;
    @MockBean
	private MessageService messageService;
    @MockBean
	private LobbyUserService lobbyUserService;
    @MockBean
    private HttpSession session;
    @MockBean
    private HttpServletRequest request;
    @MockBean
    private FriendService friendService;
    @MockBean
    private InvitationService invitationService;

    private final String VIEWS_REDIRECT_HOME = "redirect:/home";
    private final String VIEWS_REDIRECT = "redirect:/";
    private final String VIEWS_REDIRECT_LOBBY = "redirect:/lobby";
    private static final String VIEWS_LOBBY = "game/lobby";


    private User userController;
    private Lobby lobby;
    List<Lobby> lobbies;
    List<User> users;
    private Invitation invitation;


    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
        userController.setEnabled(true);

        lobby = new Lobby();
        lobby.setId(1);
        lobby.generatorCode();
        lobby.setActive(true);

        users = new ArrayList<>();
        users.add(userController);

        lobbies = new ArrayList<>();
        lobbies.add(lobby);

        invitation = new Invitation();
        invitation.setId(0);
        invitation.setLobby(lobby);
        invitation.setMode(Mode.VIEWER);
        invitation.setReceiver(userController);
        invitation.setSender(userController);

        
    }

    @WithMockUser(value = "spring")
    @Test
    public void createLobbyCheckUserTrueTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        
        mockMvc.perform(get("/lobby/create").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void createLobbyCheckGameTrueTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(lobbyUserService.checkUserLobby(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(true);
  
        mockMvc.perform(get("/lobby/create").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void createLobbyTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(lobbyUserService.checkUserLobby(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);
  
        mockMvc.perform(get("/lobby/create").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_LOBBY));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinCheckUserTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUser(any(), any())).willReturn(true);
  
        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUser(any(), any())).willReturn(false);
  
        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinExceptionTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUser(any(), any())).willThrow(new IllegalArgumentException(""));
  
        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void validateJoinTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("code", "erjvbnñ4rjbv");

        List<String> errores = new ArrayList<>();
        errores.add("error 1");
        
        given(userService.checkUser(any(), any())).willReturn(false);
        given(userService.checkUserNoExists( any())).willReturn(false);
        given(gameService.checkLobbyErrors(any())).willReturn(errores);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn( true);
        given(gameService.findGameByUserAndActive(any(), any())).willReturn(Optional.empty());
        given(lobbyUserService.findUsersByLobbyAndMode(any(),any())).willReturn(users);
        
        given(friendService.findUserFriends(any(),any())).willReturn(users);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("selectedCards")).willReturn("");
  
        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_LOBBY));

    }

   /*  @WithMockUser(value = "spring")
    @Test
    public void validateJoinWithoutErrorsTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("code", "");

        List<String> errores = new ArrayList<>();
        
        given(userService.checkUserNoExists( any())).willReturn(false);
        given(userService.checkUser(any(), any())).willReturn(false);
        given(gameService.checkLobbyErrors(any())).willReturn(errores);
  
        mockMvc.perform(get("/join").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_LOBBY));

    }*/

    @WithMockUser(value = "spring")
    @Test
    public void ejectPlayerTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(lobbyUserService.findLobbiesByUser(any())).willReturn(lobbies);
        given(lobbyUserService.findUsersByLobby(any())).willReturn(users);
  
        mockMvc.perform(get("/lobby/delete/{idEjectedUser}", userController.getId()).with(csrf()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void ejectPlayerHostDistinctTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", new User());
        
        
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(lobbyUserService.findLobbiesByUser(any())).willReturn(lobbies);
        given(lobbyUserService.findUsersByLobby(any())).willReturn(users);
  
        mockMvc.perform(get("/lobby/delete/{idEjectedUser}", userController.getId()).with(csrf()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_LOBBY));

    }

    @WithMockUser(value = "spring")
    @Test
    public void ejectPlayerGameServiceTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(lobbyUserService.findLobbiesByUser(any())).willReturn(lobbies);
        given(lobbyUserService.findUsersByLobby(any())).willReturn(users);
        given(gameService.ejectPlayer(any(),any())).willReturn(true);
        
        mockMvc.perform(get("/lobby/delete/{idEjectedUser}", userController.getId()).with(csrf()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_LOBBY));

    }

    @WithMockUser(value = "spring")
    @Test
    public void ejectPlayerExceptionTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(lobbyUserService.findLobbiesByUser(any())).willReturn(lobbies);
        given(gameService.ejectPlayer(any(),any())).willThrow(new Exception());


        mockMvc.perform(get("/lobby/delete/{idEjectedUser}", userController.getId()).with(csrf()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinLobbyCheckUserTrueTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUserNoExists(any())).willReturn(true);
        
        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinLobbyGameCheckUserTrueTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(lobbyUserService.checkUserLobby(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);

        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinLobbyExceptionTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyUserService.findLobbyByUser(any())).willReturn(lobby);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("selectedCards")).willReturn("");
        given(lobbyUserService.findLobbyByUserAndMode(any(), any())).willReturn(lobby);
        given(friendService.findUserFriends(any(), any())).willReturn(users);


        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void joinLobbyTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyUserService.findLobbyByUser(any())).willReturn(lobby);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("selectedCards")).willReturn("");
        given(lobbyUserService.findLobbyByUserAndMode(any(), any())).willReturn(lobby);
        given(friendService.findUserFriends(any(), any())).willReturn(users);
        given(lobbyUserService.findUsersByLobbyAndMode(any(), any())).willReturn(users);

        mockMvc.perform(get("/lobby").flashAttrs(atr))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_LOBBY));

    }

    @WithMockUser(value = "spring")
    @Test
    public void acceptInvitationTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(invitationService.findInvitationById(any())).willReturn(Optional.of(invitation));
        given(request.getSession()).willReturn(session);
     

        mockMvc.perform(get("/lobby/accept/{idInvitationReceiver}", userController.getId()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_LOBBY));

    }

    @WithMockUser(value = "spring")
    @Test
    public void acceptInvitationNoPresentTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        
        given(invitationService.findInvitationById(any())).willReturn(Optional.empty());
        given(request.getSession()).willReturn(session);
     

        mockMvc.perform(get("/lobby/accept/{idInvitationReceiver}", userController.getId()).flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

}
