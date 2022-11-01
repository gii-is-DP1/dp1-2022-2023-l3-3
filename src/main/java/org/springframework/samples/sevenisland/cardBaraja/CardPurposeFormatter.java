package org.springframework.samples.sevenisland.cardBaraja;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CardPurposeFormatter implements Formatter<CardPurpose> {

    @Autowired
    private CardBarajaService cardBarajaService;

    @Override
    public String print(CardPurpose object, Locale locale) {
        return object.getPurpose();
    }

    @Override
    public CardPurpose parse(String text, Locale locale) throws ParseException {
        for(CardPurpose cp : cardBarajaService.getAllCardPurposes()){
            if(cp.getPurpose().equals(text)){
                return cp;
            }
        }
        throw new ParseException("No se ha encontardo el propisto de la carta", 0);
    }
    
}
