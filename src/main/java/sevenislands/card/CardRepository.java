package sevenislands.card;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Integer> {

    // @Query("SELECT ctype FROM CardType ctype")
    // public List<CardType> findCardTypes() throws DataAccessException;

    // @Query("SELECT card FROM Card card WHERE card.type=?1")
    // public List<Card> findCardsByType(String type) throws DataAccessException;
}
