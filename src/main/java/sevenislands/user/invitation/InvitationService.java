package sevenislands.user.invitation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.Mode;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;
import sevenislands.user.UserService;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;
    private final LobbyUserService lobbyUserService;
    
    @Autowired
    public InvitationService(InvitationRepository invitationRepository, UserService userService,
        LobbyUserService lobbyUserService) {
        this.invitationRepository = invitationRepository;
        this.userService = userService;
        this.lobbyUserService = lobbyUserService;
    }

    @Transactional
    public Invitation inviteUser(String modeString, Integer idUserInvited, User logedUser) {
        Invitation invitation = new Invitation();
        try {
            Mode mode = Mode.valueOf(modeString.toUpperCase());
            Optional<User> userInvited = userService.findUserById(idUserInvited);
            if(userInvited.isPresent()) {
                if(!checkInvitation(logedUser, userInvited.get())) {
                    Lobby lobby = lobbyUserService.findLobbyByUser(logedUser);
                    invitation.setMode(mode);
                    invitation.setSender(logedUser);
                    invitation.setReceiver(userInvited.get());
                    invitation.setLobby(lobby);
                    invitationRepository.save(invitation);
                } else {
                    Optional<Invitation> invitationFound = findInvitation(logedUser, userInvited.get());
                    if(invitationFound.isPresent()) {
                        invitationFound.get().setMode(mode);
                        invitationRepository.save(invitationFound.get());
                    }
                }
            }
            return invitation;
        } catch (Exception e) {
            return invitation;
        }
    }

    @Transactional
    public Boolean checkInvitation(User sender, User receiver) {
        Optional<List<Invitation>> invitation = invitationRepository.findBySenderAndReceiver(sender, receiver);
        if(invitation.isPresent()) {
            return invitationRepository.findBySenderAndReceiver(sender, receiver).get().size() > 0;
        }
        return false;
    }

    @Transactional
    public Optional<Invitation> findInvitation(User sender, User receiver) {
        Optional<List<Invitation>> invitation = invitationRepository.findBySenderAndReceiver(sender, receiver);
        if(invitation.isPresent()) {
            return Optional.of(invitationRepository.findBySenderAndReceiver(sender, receiver).get().get(0));
        }
        return Optional.empty();
    }

    @Transactional
    public List<Invitation> findInvitationsByReceiver(User receiver) {
        Optional<List<Invitation>> invitations = invitationRepository.findByReceiver(receiver);
        if(invitations.isPresent()) {
            return invitations.get();
        }
        return new ArrayList<>();
    }

    @Transactional
    public Optional<Invitation> findInvitationById(Integer id) {
        return invitationRepository.findById(id);
    }

    @Transactional
    public Boolean checkUser(HttpServletRequest request, User user) throws ServletException {
        try {
            HttpSession session = request.getSession();
            session.setAttribute("usersList", new ArrayList<User>());
            Lobby lobby = lobbyUserService.findLobbyByUser(user);
            Boolean result = userService.checkUser(request, user);
            deleteInvitationsByLobbyAndUser(lobby, user);
            return result;
        } catch (Exception e) {
            return false;
        }
        
    }

    @Transactional
    public void deleteInvitationsByLobbyAndUser(Lobby lobby, User user) {
        Optional<List<Invitation>> invitation = invitationRepository.findInvitationsByLobbyAndUser(lobby, user);
        if(invitation.isPresent()) {
            invitationRepository.deleteAll(invitation.get());
        }
    }
}
