package sevenislands.card;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.Game;
import sevenislands.treasure.Treasure;
import sevenislands.treasure.TreasureService;

@Service
public class CardService {
    
    private CardRepository cardRepository;
    private final TreasureService TreasureService;

    @Autowired
    public CardService(CardRepository cardRepository, TreasureService TreasureService) {
        this.cardRepository = cardRepository;
        this.TreasureService = TreasureService;
    }

    @Transactional
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Transactional
    public void initGameCards(Game game) {
        for (Treasure c : TreasureService.findAllTreasures()) {
            Card card = new Card();
            if(c.getId()==1) card.setMultiplicity(27);
            if(c.getId()==2 || c.getId()==3 || c.getId()==4) card.setMultiplicity(3);
            if(c.getId()==5 || c.getId()==6 || c.getId()==7) card.setMultiplicity(4);
            if(c.getId()==8 || c.getId()==9 || c.getId()==10) card.setMultiplicity(6);
            card.setTreasure(c);
            card.setGame(game);
            cardRepository.save(card);
        }
    }

    @Transactional
    public List<Card> findAllCardsByGameId(Integer gameId) {
        return cardRepository.findAllByGameId(gameId);
    }

    @Transactional
    public Card findCardByGameAndTreasure(Integer gameId, String treasure) {
        return cardRepository.findByGameAndTreasure(gameId, treasure);

    }
}
