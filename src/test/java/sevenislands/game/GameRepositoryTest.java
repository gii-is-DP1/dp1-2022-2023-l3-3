package sevenislands.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sevenislands.lobby.Lobby;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testFindGameByLobbyId() {
        
        Lobby lobby = new Lobby();
        lobby.setId(1);
        Game game = new Game();
        game.setLobby(lobby);
        game.setCreationDate(LocalDateTime.now());
        entityManager.persist(game);
        entityManager.flush();
    

        
        Optional<Game> foundGame = gameRepository.findGameByLobbyId(2);

        
        assertTrue(foundGame.isPresent());
       
    }

    @Test
    public void testFindGameByLobby() {
        
        Lobby lobby = new Lobby();
        lobby.setCode("password");
        entityManager.persist(lobby);
        entityManager.flush();

        Game game = new Game();
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        entityManager.persist(game);
        entityManager.flush();

        
        Optional<Game> foundGame = gameRepository.findGameByLobby(lobby);

        
        assertTrue(foundGame.isPresent());
        assertEquals(game, foundGame.get());
}

@Test
public void testFindGamesByLobbies() {
    
    List<Lobby> lobbies = new ArrayList<>();
    Lobby lobby1 = new Lobby();
    lobby1.setCode("password");
    entityManager.persist(lobby1);
    entityManager.flush();
    lobbies.add(lobby1);

    Lobby lobby2 = new Lobby();
    lobby2.setCode("password2");
    entityManager.persist(lobby2);
    entityManager.flush();
    lobbies.add(lobby2);

    Lobby lobby3 = new Lobby();
    lobby3.setCode("password3");
    entityManager.persist(lobby3);
    entityManager.flush();
    lobbies.add(lobby3);

    

    List<Game> games = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
        Game game = new Game();
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobbies.get(i));
        entityManager.persist(game);
        games.add(game);
    }
    entityManager.flush();

    
    Optional<List<Game>> foundGames = gameRepository.findGamesByLobbies(lobbies);

    
    assertTrue(foundGames.isPresent());
    assertFalse(foundGames.get().isEmpty());

    for (Game game : foundGames.get()) {
        assertTrue(lobbies.contains(game.getLobby()));
    }
}



@Test
public void findGameByLobbyAndActive_returnsGamesByLobbyAndActive() {
    //Arrange
    Lobby lobby1 = new Lobby();
    lobby1.setCode("password1");
    entityManager.persist(lobby1);
    Lobby lobby2 = new Lobby();
    lobby2.setCode("password2");
    entityManager.persist(lobby2);
    
    Game game1 = new Game();
    game1.setCreationDate(LocalDateTime.now());
    game1.setLobby(lobby1);
    game1.setActive(true);
    entityManager.persist(game1);

    Game game2 = new Game();
    game2.setCreationDate(LocalDateTime.now());
    game2.setLobby(lobby2);
    game2.setActive(true);
    entityManager.persist(game2);

    
    List<Lobby> lobbies = Arrays.asList(lobby1, lobby2);

    //Act
    Optional<List<Game>> actualGames = gameRepository.findGameByLobbyAndActive(lobbies, true);

    //Assert
    assertTrue(actualGames.isPresent());
    List<Game> games = actualGames.get();
    assertEquals(2, games.size());
    assertEquals(game1, games.get(1));
    assertEquals(game2, games.get(0));
    }
}



