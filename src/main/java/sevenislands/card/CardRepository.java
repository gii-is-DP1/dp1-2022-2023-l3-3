package sevenislands.card;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
    
    @Query("SELECT card FROM Card card WHERE card.game.id=?1")
    public List<Card> findAllByGameId(Integer gameId);

    @Query("SELECT card FROM Card card WHERE card.game.id=?1 AND card.treasure.name=?2")
    public Card findByGameAndTreasure(Integer gameId, String treasure);
}
