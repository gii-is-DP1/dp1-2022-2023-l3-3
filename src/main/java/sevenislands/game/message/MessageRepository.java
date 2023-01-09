package sevenislands.game.message;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sevenislands.game.Game;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{
    
    @Query("SELECT message FROM Message message WHERE message.game = ?1")
    public List<Message> findByGame(Game game);
}
