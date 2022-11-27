package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Test
    public void retrieveGameByLobbyIdSuccessful() {
        Game game = gameRepository.findGamebByLobbyId(1).get();
        assertNotNull(game, "El repositorio ha devuelto un objeto nulo");
        assertEquals(1, game.getLobby().getId());
    }

    @Test
    public void checkNumOfGames() {
        Iterable<Game> gameIter = gameRepository.findAll();
        List<Game> games = new ArrayList<>();
        for(Game g : gameIter) {
            games.add(g);
        }
        Integer numGames = gameRepository.getNumOfGames();
        assertEquals(games.size(), numGames, "El repositorio no ha devuelto el número de games esperado");
    }


    @Test
    public void TestFindLobbyByNicknameSuccess(){
        Optional<Game> game = gameRepository.findGameByNickname("player1");
        assertNotNull(game);
    }  

}
