package org.springframework.samples.sevenislands.card;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Integer> {

    /**
     * Retrieve all <code>Card</code> from the data store.
     * 
     * @return a <code>Collection</code> of <code>Card</code>
     */
    List<Card> findAll() throws DataAccessException;

    /**
     * Retrieve a <code>Card</code> from the data store by id.
     * 
     * @param id the id to search for
     * @return the <code>Card</code> if found
     * @throws DataAccessException
     */
    Card findById(int id) throws DataAccessException;

    /**
     * Retrieve all <code>CardType</code> from the data store.
     * 
     * @return a <code>Collection</code> of <code>CardType</code>
     * @throws DataAccessException
     */
    @Query("SELECT ctype FROM CardType ctype")
    List<CardType> findCardTypes() throws DataAccessException;

    /**
     * Retrieve all <code>Card</code> from the data store where
     * card's type is equal to param type.
     * 
     * @param type the type to search for
     * @return a <code>Collection</code> of <code>CardType</code>
     * @throws DataAccessException
     */
    @Query("SELECT card FROM Card card WHERE card.type=:?1")
    List<Card> findCardsByType(String type) throws DataAccessException;
}
