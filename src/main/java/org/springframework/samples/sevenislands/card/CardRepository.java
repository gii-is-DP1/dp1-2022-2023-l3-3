package org.springframework.samples.sevenislands.card;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Integer> {
    
}
