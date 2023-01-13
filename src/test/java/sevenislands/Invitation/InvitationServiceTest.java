package sevenislands.Invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sevenislands.enums.Mode;
import sevenislands.enums.UserType;
import sevenislands.game.GameRepository;
import sevenislands.game.GameService;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;
import sevenislands.user.invitation.Invitation;
import sevenislands.user.invitation.InvitationRepository;
import sevenislands.user.invitation.InvitationService;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private LobbyUserRepository lobbyUserRepository;
    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private RoundRepository roundRepository;

    private UserService userService;
    private LobbyService lobbyService;
    private LobbyUserService lobbyUserService;
    private InvitationService invitationService;
    private GameService gameService;
    private RoundService roundService;

    private User userController;
    private Invitation invitation;
    private Lobby lobby;
    private List<Invitation> invitations;

    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
        userController.setEnabled(true);

        roundService = new RoundService(roundRepository);
        gameService = new GameService(roundService, gameRepository, lobbyUserService, lobbyService);
        userService = new UserService(null, null, null, userRepository, gameService, lobbyUserService);
        lobbyService = new LobbyService(lobbyRepository);
        lobbyUserService = new LobbyUserService(lobbyUserRepository, lobbyService);
        invitationService = new InvitationService(invitationRepository, userService, lobbyUserService);
        
        lobby = new Lobby();
        lobby.setId(1);
        lobby.generatorCode();
        lobby.setActive(true);

        invitation = new Invitation();
        invitation.setId(0);
        invitation.setLobby(lobby);
        invitation.setMode(Mode.VIEWER);
        invitation.setReceiver(userController);
        invitation.setSender(userController);

        invitations = new ArrayList<>();
        invitations.add(invitation);
    }

    @Test
    public void checkInvitationTest(){
        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.empty());
        assertFalse(invitationService.checkInvitation(userController, userController));
        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.of(invitations));
        assertTrue(invitationService.checkInvitation(userController, userController));
    }

    @Test
    public void findInvitationTest(){
        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(),invitationService.findInvitation(userController, userController));
        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.of(invitations));
        assertEquals(Optional.of(invitation), invitationService.findInvitation(userController, userController));
    }


    @Test
    public void findInvitationsByReceiverTest(){
        when(invitationRepository.findByReceiver(any())).thenReturn(Optional.empty());
        assertTrue(invitationService.findInvitationsByReceiver(userController).isEmpty());
        when(invitationRepository.findByReceiver(any())).thenReturn(Optional.of(invitations));
        assertTrue(invitationService.findInvitationsByReceiver(userController).size() > 0);
    }

    @Test
    public void findInvitationsByIdTest(){
        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        assertTrue(invitationService.findInvitationById(userController.getId()).get().getMode().equals(Mode.VIEWER));
    }

    @Test
    public void deleteInvitationBylobbyAndUserTest(){
        when(invitationRepository.findInvitationsByLobbyAndUser(any(), any())).thenReturn(Optional.empty());
        assertFalse(invitationService.deleteInvitationsByLobbyAndUser(lobby, userController));
        when(invitationRepository.findInvitationsByLobbyAndUser(any(), any())).thenReturn(Optional.of(invitations));
        assertTrue(invitationService.deleteInvitationsByLobbyAndUser(lobby, userController));
    }

    @Test
    public void inviteUserTest(){
        List<LobbyUser> lobbyUsers = new ArrayList<>();
        LobbyUser lobbyUser = new LobbyUser();
        lobbyUser.setId(1);
        lobbyUser.setLobby(lobby);
        lobbyUser.setMode(Mode.VIEWER);
        lobbyUser.setUser(userController);
        lobbyUsers.add(lobbyUser); 

        when(invitationRepository.save(any())).thenReturn(invitation);

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertTrue(invitationService.inviteUser("VIEWER", userController.getId(), userController).getLobby()==null);

        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.of(invitations));
        when(userRepository.findById(any())).thenReturn(Optional.of(userController));
        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.of(invitations));
        assertTrue(invitationService.inviteUser("VIEWER", userController.getId(), userController).getMode().equals(Mode.VIEWER));

        when(invitationRepository.findBySenderAndReceiver(any(),any())).thenReturn(Optional.empty());
        when(lobbyUserRepository.findByUser(any())).thenReturn(Optional.of(lobbyUsers));
        assertTrue(invitationService.inviteUser("VIEWER", userController.getId(), userController).getMode().equals(Mode.VIEWER));
       
        when(userRepository.findById(any())).thenThrow(new IllegalArgumentException(""));
        assertTrue(invitationService.inviteUser("VIEWER", userController.getId(), userController).getLobby()==null);
    
    }





    
}
