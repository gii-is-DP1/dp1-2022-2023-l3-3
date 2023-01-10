package sevenislands.user.friend;

import java.util.List;
import java.util.Optional;

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
    public void acceptFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && requestExists(logedUser, friend.get().getUser1())) {
            friend.get().setStatus(Status.ACCEPTED);
            friendRepository.save(friend.get());
        }
    }

    @Transactional
    public void rejectFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && requestExists(logedUser, friend.get().getUser1())) {
            friend.get().setStatus(Status.REJECTED);
            friendRepository.save(friend.get());
        }
    }

    @Transactional
    public void deleteFriend(Integer id, User logedUser) {
        Optional<Friend> friend = friendRepository.findById(id);
        if(friend.isPresent() && (requestExists(logedUser, friend.get().getUser1()) || requestExists(logedUser, friend.get().getUser2()))) {
            friendRepository.delete(friend.get());
        }
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
}
