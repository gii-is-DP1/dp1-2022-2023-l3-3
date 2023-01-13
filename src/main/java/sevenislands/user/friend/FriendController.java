package sevenislands.user.friend;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import sevenislands.enums.Status;
import sevenislands.user.User;
import sevenislands.user.UserService;

@Controller
public class FriendController {
    
    private final FriendService friendService;
    private final UserService userService;

    @Autowired
    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @GetMapping("/friends")
    public String getFriends(HttpServletRequest request, ModelMap model, @ModelAttribute("logedUser") User logedUser) {
        HttpSession session = request.getSession();
        List<Friend> friends = friendService.findFriends(logedUser, Status.ACCEPTED);
        List<User> friendsList = friends.stream().map(friend -> friend.getUser1() == logedUser? friend.getUser2() : friend.getUser1()).collect(Collectors.toList());
        List<User> usersList = (List<User>) session.getAttribute("usersList");
        if (usersList != null) usersList.removeAll(friendsList);   
        
        model.put("logedUser", logedUser);
        model.put("searchedUser", new User());
        model.put("searchResults", usersList);
        model.put("friends", friends);
        model.put("sentRequests", friendService.findFriendsFrom(logedUser, Status.PENDING));
        model.put("rejected", friendService.findFriendsTo(logedUser, Status.REJECTED));
        model.put("receivedRequests", friendService.findFriendsTo(logedUser, Status.PENDING));
        return "friend/friendsPage";
    }

    @GetMapping("/friends/search")
    public String searchUser(HttpServletRequest request, ModelMap model, @ModelAttribute("searchedUser") User searchedUser) {
        User logedUser = userService.getCurrentUser();

        List<User> usersList = userService.searchUserByNickname(logedUser.getNickname(), searchedUser.getNickname());

        HttpSession session = request.getSession();
        session.setAttribute("usersList", usersList);

        return "redirect:/friends";
    }

    @GetMapping("/friends/add/{idUserSend}")
    public String addFriend(ModelMap model, @ModelAttribute("logedUser") User logedUser, @PathVariable("idUserSend") Integer id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent() && !friendService.requestExists(logedUser, user.get())) {
            friendService.addFriend(logedUser, user.get());
        }
        return "redirect:/friends";
    }

    @GetMapping("/friends/delete/{friendId}")   
    public String deleteFriend(ModelMap model, @ModelAttribute("logedUser") User logedUser, @PathVariable("friendId") Integer id) {
        friendService.deleteFriend(id, logedUser);
        return "redirect:/friends";
    }

    @GetMapping("/friends/accept/{friendId}")
    public String acceptFriend(ModelMap model, @ModelAttribute("logedUser") User logedUser, @PathVariable("friendId") Integer id) {
        friendService.acceptFriend(id, logedUser);
        return "redirect:/friends";
    }

    @GetMapping("/friends/reject/{friendId}")
    public String rejectFriend(ModelMap model, @ModelAttribute("logedUser") User logedUser, @PathVariable("friendId") Integer id) {
        friendService.rejectFriend(id, logedUser);
        return "redirect:/friends";
    }
}
