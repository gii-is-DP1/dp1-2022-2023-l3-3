package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.lobby.Lobby;

@DataJpaTest
public class GameServiceTest {
    
    @Autowired
    private GameRepository gameRepository;


    @Test
    public void TestGameCount(){
        GameService gameService=new GameService(gameRepository);
        Integer numGames=gameService.gameCount();
        assertTrue(numGames>=1);
    }


    @Test
    public void TestSave(){
        GameService gameService=new GameService(gameRepository);
        Game game=new Game();
        Date fecha1=new Date(2002, 2, 3);
        Date fecha2=new Date(2004, 2, 3);
        Lobby lobby=new Lobby();
        game.setId(8);
        game.setCreationDate(fecha1);
        game.setEndingDate(fecha2);
        game.setLobby(lobby);
        gameService.save(game);
        assertNotNull(game);
    }


    @Test 
    public void TestFindGamebByLobbyId(){
        GameService gameService=new GameService(gameRepository);
        Optional<Game> game=gameService.findGamebByLobbyId(1);
        assertNotNull(game.get());
        assertTrue(game.get().getId().equals(1));
    }

}
