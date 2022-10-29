package org.springframework.samples.sevenislands.card;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.sevenislands.Enums.TipoCarta;

public interface CardRepository extends CrudRepository<Card, Integer> {
    
    @Query("Select distinc card from Card card where card.TipoCarta = :tipoCarta")
    public Collection<Card> findByCardType(@Param("active") TipoCarta tipoCarta);

}
