package sevenislands.card;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.Tipo;
import sevenislands.game.Game;


@Service
public class CardService {
    
    private final CardRepository cardRepository;
   

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    @Transactional
    public void delete(Card card){
        cardRepository.delete(card);
    }

    @Transactional
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Transactional
        public Card findCardById(Integer id) {
         return cardRepository.findById(id).get();
    }
    @Transactional
    public void initGameCards(Game game) {
        for (Tipo c : Tipo.values()) {
            Card card = new Card();
<<<<<<< HEAD
            if(c==Tipo.Doblon) card.setMultiplicity(27);
            if(c==Tipo.Caliz || c==Tipo.Rubi || c==Tipo.Diamante) card.setMultiplicity(3);
            if(c==Tipo.Collar || c==Tipo.MapaTesoro || c==Tipo.Corona) card.setMultiplicity(4);
            if(c==Tipo.Revolver || c==Tipo.Espada || c==Tipo.BarrilRon) card.setMultiplicity(6);
            card.setTipo(c);
            card.setGame(game);
            cardRepository.save(card);
           
=======
            if(c==c.Doblon) card.setMultiplicity(27);
            if(c==c.Caliz || c==c.Rubi || c==c.Diamante) card.setMultiplicity(3);
            if(c==c.Collar || c==c.MapaTesoro || c==c.Corona) card.setMultiplicity(4);
            if(c==c.Revolver || c==c.Espada || c==c.BarrilRon) card.setMultiplicity(6);
            card.setTipo(c);
            card.setGame(game);
            cardRepository.save(card);
>>>>>>> 935c036c6c38b5066c4fe22ce19a08dd2e3e0722
        }
    }

    @Transactional
    public List<Card> findAllCardsByGameId(Integer gameId) {
        return cardRepository.findAllByGameId(gameId);
    }

    @Transactional
    public Card findCardByGameAndTreasure(Integer gameId, Tipo tipo) {
        return cardRepository.findByGameAndTreasure(gameId, tipo);

    }

    @Transactional
    public Card findCardByTreasureName(Tipo tipo){
        return cardRepository.findCardByTreasureName(tipo);
    }

    @Transactional
    public List<Card> findCardOrderByMultiplicity(){
        return cardRepository.findCardOrderByMultiplicity();
    }
}

