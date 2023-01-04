package sevenislands.card;

import org.springframework.stereotype.Repository;

import sevenislands.enums.Tipo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
    
    @Query("SELECT card FROM Card card WHERE card.game.id=?1")
    public List<Card> findAllByGameId(Integer gameId);
 
    @Query("SELECT card FROM Card card WHERE card.game.id=?1 AND card.tipo=?2")
    public Card findByGameAndTreasure(Integer gameId,Tipo tipo);
 
    @Query("Select card FROM Card card WHERE card.tipo=?1")
    public Card findCardByTreasureName(Tipo tipo);

    @Query("SELECT card FROM Card card ORDER BY card.multiplicity DESC")
    public List<Card> findCardOrderByMultiplicity();
}
