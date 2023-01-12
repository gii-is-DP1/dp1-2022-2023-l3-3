package sevenislands.user.friend;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.Status;
import sevenislands.user.User;

@Service
public class FriendService {
    
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }
    
    @Transactional
    public void addFriend(User userSend, User userReceive) {
        Friend friend = new Friend();
        friend.setUser1(userSend);
        friend.setUser2(userReceive);
        friend.setStatus(Status.PENDING);
        friendRepository.save(friend);
    }

    @Transactional
    public Optional<Friend> acceptFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && requestExists(logedUser, friend.get().getUser1())) {
            friend.get().setStatus(Status.ACCEPTED);
            friendRepository.save(friend.get());
        }
        return friend;
    }

    @Transactional
    public Optional<Friend> rejectFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && requestExists(logedUser, friend.get().getUser1())) {
            friend.get().setStatus(Status.REJECTED);
            friendRepository.save(friend.get());
        }
        return friend;
    }

    @Transactional
    public  Optional<Friend> deleteFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && (requestExists(logedUser, friend.get().getUser1()) || requestExists(logedUser, friend.get().getUser2()))) {
            friendRepository.delete(friend.get());
        }
        return friend;
    }

    @Transactional
    public Boolean requestExists(User userSend, User userReceive) {
        Optional<Friend> friend = friendRepository.findByUser1AndUser2(userSend, userReceive);
        if(friend.isPresent()) {
            return true;
        }
        return false;
    }

    @Transactional
    public List<Friend> findFriends(User user, Status status) {
        return friendRepository.findFriends(user, status);
    }

    @Transactional
    public List<Friend> findFriendsTo(User user, Status status) {
        return friendRepository.findFriendsTo(user, status);
    }

    @Transactional
    public List<Friend> findFriendsFrom(User user, Status status) {
        return friendRepository.findFriendsFrom(user, status);
    }

    @Transactional
    public Optional<Friend> findFriendById(Integer id) {
        return friendRepository.findById(id);
    }

    @Transactional
    public List<User> findUserFriends(User user, Status status) {
        List<Friend> friends = friendRepository.findFriends(user, status);
        List<User> users = friends.stream().map(friend -> friend.getUser1().equals(user)? friend.getUser2() : friend.getUser1()).collect(Collectors.toList());
        return users;
    }
}
