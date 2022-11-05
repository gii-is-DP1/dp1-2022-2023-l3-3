package org.springframework.samples.petclinic.cardBaraja;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardBarajaService {
    
    @Autowired
    private CardBarajaRepository cardRepository;

    public Collection<CardBaraja> getCardsByType(CardPurpose cardPurpose){
        return cardRepository.findByCardPurpose(cardPurpose);
    }
    
    public Collection<CardType> getAllCardTypes(){
        return cardRepository.findAllCardTypes();
    }

    public Collection<CardPurpose> getAllCardPurposes(){
        return cardRepository.findAllCardPurposes();
    }

}
