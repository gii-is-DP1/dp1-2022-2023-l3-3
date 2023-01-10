package sevenislands.user.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sevenislands.user.User;

@Controller
public class InvitationController {
    
    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/invite/{mode}/{userInvited}")
    public String inviteUser(@ModelAttribute("logedUser") User logedUser, @PathVariable("mode") String mode, @PathVariable("userInvited") Integer idUserInvited) {
        invitationService.inviteUser(mode, idUserInvited, logedUser);
        return "redirect:/lobby";
    }
}
