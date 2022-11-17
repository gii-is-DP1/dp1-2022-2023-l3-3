package sevenislands.card;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CardServiceTest {

    @Autowired
    private CardRepository cardRepository;

   
    @Test
    public void TestFindAllCards(){
        CardService cardService=new CardService(cardRepository);
        List<Card> cards=cardService.findAllCards();
        assertNotNull(cards);
    }    
}
