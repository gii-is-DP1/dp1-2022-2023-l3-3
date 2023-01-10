package sevenislands.round;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.card.CardRepository;
import sevenislands.game.GameRepository;
import sevenislands.game.island.IslandRepository;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.lobby.LobbyRepository;
import sevenislands.user.UserRepository;

@DataJpaTest
public class RoundRepositoryTest {
    

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IslandRepository islandRepository;

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    GameRepository gameRepository;

/* 
    @BeforeEach
    public void config(){
        try {
            Lobby lobby = new Lobby();
        List<User> users = userRepository.findAll().stream().filter(u -> u.getUserType().equals("player")).limit(3).collect(Collectors.toList());
        lobby.setUsers(users);
        lobby.setCode(lobby.generatorCode());
        lobby.setActive(true);
        lobbyRepository.save(lobby);
        Island island = new Island();
        Card card = new Card();
        Game game = new Game();
        game.setActive(true);
        game.setId(99);
        game.setCreationDate(new Date(System.currentTimeMillis()));
        game.setLobby(lobby);
        gameRepository.save(game);
        card.setMultiplicity(4);
        card.setTipo(Tipo.Caliz);
        card.setGame(game);
        cardRepository.save(card);
        island.setGame(game);
        island.setCard(card);
        island.setNum(1);
        island.setId(999);
        islandRepository.save(island);
        Round round = new Round();
        round.setGame(game);
        roundRepository.save(round);
        } catch (Exception e) {
            int i = 0;
           System.out.println(e);
        }
        
    }*/

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Round> round = roundRepository.findAll();
    

        assertNotNull(round);
        assertFalse(round.isEmpty());
        assertEquals(15, round.size());
    }


    @Test
    public void TestFindRoundByGameId(){
        List<Round> round=roundRepository.findRoundByGameId(1);

        assertNotNull(round);
        assertEquals(15, round.size());
    }
}
