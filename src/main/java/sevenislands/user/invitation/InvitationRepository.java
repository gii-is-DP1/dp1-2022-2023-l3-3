package sevenislands.user.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.user.User;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Integer> {
    
    @Query("SELECT invitation FROM Invitation invitation WHERE invitation.sender = ?1 AND invitation.receiver = ?2 ORDER BY invitation.id DESC")
    public Optional<List<Invitation>> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT invitation FROM Invitation invitation WHERE invitation.receiver = ?1 ORDER BY invitation.id DESC")
    public Optional<List<Invitation>> findByReceiver(User receiver);
}
