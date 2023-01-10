package sevenislands.round;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.game.Game;
import sevenislands.game.GameRepository;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;

@DataJpaTest
public class RoundServiceTest {
    
    @Mock
    RoundRepository roundRepository;
    @Mock
    GameRepository gameRepository;

    RoundService roundService;
    
    Lobby lobby;
    Game game;
    Round round;

    @BeforeEach
    public void config(){
        roundService = new RoundService(roundRepository);

        lobby = new Lobby();
        lobby.setId(1);
        lobby.generatorCode();
        lobby.setActive(true);

        game = new Game();
        game.setId(1);
        game.setCreationDate(LocalDateTime.now());
        game.setActive(true);
        game.setLobby(lobby);

        round = new Round();
        round.setId(1);
        round.setGame(game);
    }

    @Test
    public void TestSave(){
        when(roundRepository.save(round)).thenReturn(round);
        round = roundService.save(round);
        assertNotNull(round);
        assertEquals(1, round.getId());
    }

    @Test
    public void TestFindAllRounds(){
        Iterable<Round> round=roundService.findAllRounds();
        List<Round>rondas=new ArrayList<>();
        round.iterator().forEachRemaining(rondas::add);
        assertNotNull(rondas);
    }

    @Test 
    public void TestFindRoundById(){
        when(roundRepository.findById(1)).thenReturn(Optional.of(round));
        Optional<Round> roundFound=roundService.findRoundById(1);
        assertNotNull(roundFound.get());
    }

    @Test
    public void TestFindRoundsByGameId(){
        Collection<Round> gameRounds = roundService.findRoundsByGame(game);
        assertNotNull(gameRounds.stream().collect(Collectors.toList()));
    }

    @Test
    public void TestCheckNoRoundByGameId(){
        when( roundRepository.findRoundByGame(game)).thenReturn(List.of(round));
        Boolean gameWithoutRound = roundService.checkRoundByGame(game);
        assertNotNull(gameWithoutRound);
        assertTrue(gameWithoutRound);

    }

}
