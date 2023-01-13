package sevenislands.turn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sevenislands.card.Card;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.Tipo;
import sevenislands.enums.UserType;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.message.MessageService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.Turn;
import sevenislands.game.turn.TurnController;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserService;

@WebMvcTest(controllers = TurnController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class TurnControllerTest {

    private User userController;
    private Game game;
    private List<Turn> turnos;
    private List<Round> rondas;
    private Round round;
    private Turn turno;
    private Lobby lobby;
    private List<Lobby> lobbies;
    private List<User> users;
    private Card card;
    private Island island;
    private List<Island> islandsList;


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

    private final String VIEWS_REDIRECT_HOME = "redirect:/home";
    private final String VIEWS_REDIRECT = "redirect:/";
    private static final String VIEWS_GAME = "game/game";
    private final String VIEWS_REDIRECT_TURN = "redirect:/turn";
    private final String VIEWS_REDIRECT_ENDGAME = "redirect:/endGame";
    private final String VIEWS_REDIRECT_NEW_ROUND = "redirect:/turn/newRound";


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
        LocalDateTime now = LocalDateTime.now();
        game.setCreationDate(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfWeek().getValue(), 0, 0));
        game.setEndingDate(now);
        game.setLobby(new Lobby());
        game.setTieBreak(false);
        game.setWinner(userController);

        turno = new Turn();
        turno.setDice(4);
        turno.setUser(userController);
        turno.setStartTime(now);


        turnos = new ArrayList<>();
        turnos.add(turno);

        round = new Round();
        round.setGame(game);
        round.setId(1);

        rondas = new ArrayList<>();
        rondas.add(round);

        lobby = new Lobby();
        lobby.setId(1);
        lobby.generatorCode();
        lobby.setActive(true);

        lobbies = new ArrayList<>();
        lobbies.add(lobby);

        users = new ArrayList<>();
        users.add(userController);

        card = new Card();
        card.setId(1);
        card.setMultiplicity(3);
        card.setTipo(Tipo.Caliz);
        card.setGame(game);
        
    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnNotExistUserTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");
        
        given(userService.checkUserNoExists(any())).willReturn(true);
        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnNotEndGametUserTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");
        
        given(turnService.endGame(any())).willReturn(true);
        given(gameService.findGameByUser(any())).willReturn(Optional.of(new Game()));
        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_ENDGAME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnNotCheckUserLobbyTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(turnService.endGame(any())).willReturn(false);
        given(gameService.findGameByUser(any())).willReturn(Optional.of(new Game()));
        given(lobbyUserService.checkUserLobby(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);


        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnNotCheckUserGameTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(turnService.endGame(any())).willReturn(false);
        given(gameService.findGameByUser(any())).willReturn(Optional.of(new Game()));
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(false);


        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnNotCheckLobbyNoAllPlayersTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(turnService.endGame(any())).willReturn(false);
        given(gameService.findGameByUser(any())).willReturn(Optional.of(new Game()));
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkLobbyNoAllPlayers(any())).willReturn(true);
        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameTurnTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(turnService.endGame(any())).willReturn(false);
        given(gameService.findGameByUser(any())).willReturn(Optional.of(new Game()));
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(gameService.checkLobbyNoAllPlayers(any())).willReturn(false);
        given(gameService.findGameByUserAndActive(any(), any())).willReturn(Optional.of(new Game()));
        given(islandService.findIslandsByGameId(any())).willReturn(new ArrayList());
        given(turnService.findByRound(any())).willReturn(turnos);
        given(roundService.findRoundsByGame(any())).willReturn(rondas);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("selectedCards")).willReturn("");
        
        mockMvc.perform(get("/turn").flashAttrs(atr))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEWS_GAME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnCheckNoUserTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(true);
        
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnCheckUserLobbyTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);
        given(lobbyUserService.checkUserLobby(any())).willReturn(false);
       
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnCheckUserGameTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        given(userService.checkUserNoExists(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(false);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
       
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnTurnSizeGreaterTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        Map<Card, Integer> cardsMap = new HashMap<>();
        request.setAttribute("selectedCards", cardsMap);
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.findGameByUserAndActive(any(), any())).willReturn(Optional.of(game));
        given(roundService.findRoundsByGame(any())).willReturn(rondas);
        given(turnService.findByRound(any())).willReturn(turnos);
        given(lobbyUserService.findLobbyByUser(any())).willReturn(lobby);
        given(lobbyUserService.findUsersByLobbyAndMode(any(), any())).willReturn(users);
        given(request.getSession()).willReturn(session);
        session.setAttribute("selectedCards", cardsMap);
        given(session.getAttribute("selectedCards")).willReturn(cardsMap);
        when(session.getAttribute("selectedCards")).thenReturn(cardsMap);
       
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_NEW_ROUND));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnTurnSizeLowerTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        Map<Card, Integer> cardsMap = new HashMap<>();
        request.setAttribute("selectedCards", cardsMap);
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.findGameByUserAndActive(any(), any())).willReturn(Optional.of(game));
        given(roundService.findRoundsByGame(any())).willReturn(rondas);
        given(turnService.findByRound(any())).willReturn(turnos);
        given(lobbyUserService.findLobbyByUser(any())).willReturn(lobby);
        given(lobbyUserService.findUsersByLobbyAndMode(any(), any())).willReturn(users);
        given(request.getSession()).willReturn(session);
        session.setAttribute("selectedCards", cardsMap);
        given(session.getAttribute("selectedCards")).willReturn(cardsMap);
        when(session.getAttribute("selectedCards")).thenReturn(cardsMap);
       users.add(userController);
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_TURN));

    }

    @WithMockUser(value = "spring")
    @Test
    public void gameEndTurnExceptionTest() throws Exception {

        Map<String, Object> atr = new HashMap<>();
        atr.put("logedUser", userController);
        atr.put("message", "");

        Map<Card, Integer> cardsMap = new HashMap<>();
        request.setAttribute("selectedCards", cardsMap);
        given(userService.checkUserNoExists(any())).willReturn(false);
        given(gameService.checkUserGame(any())).willReturn(true);
        given(lobbyUserService.checkUserLobby(any())).willReturn(true);
        given(gameService.findGameByUserAndActive(any(), any())).willReturn(Optional.of(game));
        given(roundService.findRoundsByGame(any())).willReturn(rondas);
        given(turnService.findByRound(any())).willReturn(turnos);
        given(lobbyUserService.findLobbyByUser(any())).willThrow(new NotExistLobbyException());
        given(lobbyUserService.findUsersByLobbyAndMode(any(), any())).willReturn(users);
        given(request.getSession()).willReturn(session);
        
       
        
        mockMvc.perform(get("/turn/endTurn").flashAttrs(atr))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name(VIEWS_REDIRECT_HOME));

    }
    
}
