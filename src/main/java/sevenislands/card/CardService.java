package sevenislands.card;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<Card> findAllCards() throws DataAccessException {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    // @Transactional(readOnly = true)
    // public Optional<Card> findCardById(Integer id) throws DataAccessException {
    //     return cardRepository.findById(id);
    // }

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
