package sevenislands.card;

import java.util.List;

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
        return cardRepository.findAll();
    }

    // @Transactional(readOnly = true)
    // public Optional<Card> findCardById(Integer id) throws DataAccessException {
    //     return cardRepository.findById(id);
    // }

    @Transactional(readOnly = true)
    public List<Card> findCardTypes() throws DataAccessException {
        return cardRepository.findAll();
    }

    // @Transactional(readOnly = true)
    // public Collection<Card> findCardsByType(String type) throws
    // DataAccessException {
    // return cardRepository.findCardsByType(type);
    // }
}
