package org.springframework.samples.sevenislands.card;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional(readOnly = true)
    public Collection<Card> findAllCards() throws DataAccessException {
        return cardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Card findCardById(int id) throws DataAccessException {
        return cardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Collection<CardType> findCardTypes() throws DataAccessException {
        return cardRepository.findCardTypes();
    }

    // @Transactional(readOnly = true)
    // public Collection<Card> findCardsByType(String type) throws
    // DataAccessException {
    // return cardRepository.findCardsByType(type);
    // }
}