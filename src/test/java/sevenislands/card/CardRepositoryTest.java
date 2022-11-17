package sevenislands.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;


    @Test
    public void TestDataAllFindAllSuccess(){
        List<Card> card= cardRepository.findAll();

        assertNotNull(card);
        assertFalse(card.isEmpty());
        assertEquals(10, card.size());
    }
    
}
