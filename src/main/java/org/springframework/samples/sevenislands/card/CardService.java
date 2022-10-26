package org.springframework.samples.sevenislands.card;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.Enums.TipoCarta;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;

    public Collection<Card> getCardsByType(TipoCarta tipoCarta){
        return cardRepository.findByCardType(tipoCarta);
    }

}
