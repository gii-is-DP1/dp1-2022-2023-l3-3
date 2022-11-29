package sevenislands.tools;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import sevenislands.user.User;
import sevenislands.user.UserService;

@ControllerAdvice
public class currentUser {
    
    private UserService userService;

    @Autowired
    public currentUser(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("logedUser")
    public User getCurrentUser(Principal principal) {
        User currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null && auth.isAuthenticated()) {
            Optional<User> user = userService.findUserByNickname(principal.getName());
            if(user.isPresent()) currentUser = user.get();
        }
        return currentUser;
    }
}
