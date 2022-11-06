package org.springframework.samples.sevenislands.cardBaraja;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardBarajaRepository extends CrudRepository<CardBaraja, Integer> {
    
    @Query("SELECT DISTINCT card FROM CardBaraja card WHERE card.cardPurpose = :cardPurpose")
    public Collection<CardBaraja> findByCardPurpose(@Param("cardPurpose") CardPurpose cardPurpose);
    
    @Query("SELECT DISTINCT type FROM CardType type")
    public Collection<CardType> findAllCardTypes();
    
    
    @Query("SELECT DISTINCT purpose FROM CardPurpose purpose")
    public Collection<CardPurpose> findAllCardPurposes();

}

