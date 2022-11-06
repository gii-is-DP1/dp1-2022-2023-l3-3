package org.springframework.samples.sevenislands.cardBaraja;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CardTypeFormater implements Formatter<CardType> {

    @Autowired
    private CardBarajaService cardBarajaService;

    @Override
    public String print(CardType object, Locale locale) {
        
        return object.getName();
    }

    @Override
    public CardType parse(String text, Locale locale) throws ParseException {
        for(CardType ct: cardBarajaService.getAllCardTypes()){
            if(ct.getName().equals(text)){
                return ct;
            }
        }
        throw new ParseException("No se reconoce el tipo de carta", 0);
    }
    
}
