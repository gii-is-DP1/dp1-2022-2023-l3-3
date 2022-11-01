package org.springframework.samples.sevenisland.cardBaraja;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CardBarajaRepository extends CrudRepository<CardBaraja, Integer> {
    
    @Query("Select distinc card from Card card where card.purose = :cardPurose")
    public Collection<CardBaraja> findByCardPurpose(@Param("cardPurose") CardPurpose cardPurpose);

    @Query("Select distinc type from Card_Type")
    public Collection<CardType> findAllCardTypes();

    @Query("Select distinc purpose from card_purpose")
    public Collection<CardPurpose> findAllCardPurposes();

}
