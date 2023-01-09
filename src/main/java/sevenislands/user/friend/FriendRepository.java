package sevenislands.user.friend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.enums.Status;
import sevenislands.user.User;

@Repository
public interface FriendRepository extends CrudRepository<Friend, Integer>{
    
    @Query("SELECT friend FROM Friend friend WHERE (friend.user1 = ?1 AND friend.user2 = ?2) OR (friend.user1 = ?2 AND friend.user2 = ?1)")
    public Optional<Friend> findByUser1AndUser2(User user1, User user2);

    @Query("SELECT friend FROM Friend friend WHERE (friend.user1 = ?1 OR friend.user2 = ?1) AND friend.status = ?2")
    public List<Friend> findFriends(User user, Status status);

    @Query("SELECT friend FROM Friend friend WHERE friend.user2 = ?1 AND friend.status = ?2")
    public List<Friend> findFriendsTo(User user, Status status);

    @Query("SELECT friend FROM Friend friend WHERE friend.user1 = ?1 AND friend.status = ?2")
    public List<Friend> findFriendsFrom(User user, Status status);
}
