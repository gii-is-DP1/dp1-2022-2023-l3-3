package sevenislands.game;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.user.User;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LobbyRepository lobbyRepository;


    @Test
    public void testFindGameByLobbyId() {
        
        Lobby lobby = new Lobby();
        lobby.setCode("password");
        lobby = lobbyRepository.save(lobby);
        Game game = new Game();
        game.setLobby(lobby);
        game.setCreationDate(LocalDateTime.now());
        gameRepository.save(game);
        Optional<Game> foundGame = gameRepository.findGameByLobbyId(2);
        assertFalse(foundGame.isPresent());
       
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
public void findGameByLobbyAndActive() {
    
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

        
        Optional<List<Game>> actualGames = gameRepository.findGameByLobbyAndActive(lobbies, true);

        
        assertTrue(actualGames.isPresent());
        List<Game> games = actualGames.get();
        assertEquals(2, games.size());
        assertEquals(game1, games.get(1));
        assertEquals(game2, games.get(0));
    }
    @Test
    public void findVictoriesByNicknameTest() {
        
        Date date1=new Date(2015,2,3);
        Date date2=new Date(2015,2,5);
        User user1 = new User();
        user1.setAvatar("default1");
        user1.setBirthDate(date1);
        user1.setNickname("Antonio");
        user1.setEmail("Antonio@");
        user1.setCreationDate(date1);
        user1.setFirstName("Antonio");
        user1.setLastName("Antonio");
        user1.setPassword("Antonio");
        entityManager.persist(user1);
        entityManager.flush();

        User user2 = new User();
        user2.setBirthDate(date2);
        user2.setAvatar("default2");
        user2.setCreationDate(date2);
        user2.setNickname("Alejandro");
        user2.setEmail("Alejandro@");
        user2.setFirstName("Alejandro");
        user2.setLastName("Alejandro");
        user2.setPassword("Alejandro");
        entityManager.persist(user2);
        entityManager.flush();

        Game game1 = new Game();
        game1.setCreationDate(LocalDateTime.now());
        game1.setWinner(user1);
        entityManager.persist(game1);
        entityManager.flush();

        Game game2 = new Game();
        game2.setCreationDate(LocalDateTime.now());
        game2.setWinner(user1);
        entityManager.persist(game2);
        entityManager.flush();

        Game game3 = new Game();
        game3.setCreationDate(LocalDateTime.now());
        game3.setWinner(user2);
        entityManager.persist(game3);
        entityManager.flush();

        
        Long victories1 = gameRepository.findVictoriesByNickname("Antonio");
        Long victories2 = gameRepository.findVictoriesByNickname("Alejandro");

       
        assertTrue(victories1==2);
        assertTrue(victories2==1);
    }

    @Test
    public void findTieBreaksByNicknameTest() {
        
        Date date1=new Date(2015,2,3);
        Date date2=new Date(2015,2,5);
        User user1 = new User();
        user1.setAvatar("default1");
        user1.setBirthDate(date1);
        user1.setNickname("Antonio");
        user1.setEmail("Antonio@");
        user1.setCreationDate(date1);
        user1.setFirstName("Antonio");
        user1.setLastName("Antonio");
        user1.setPassword("Antonio");
        entityManager.persist(user1);
        entityManager.flush();

        User user2 = new User();
        user2.setBirthDate(date2);
        user2.setAvatar("default2");
        user2.setCreationDate(date2);
        user2.setNickname("Alejandro");
        user2.setEmail("Alejandro@");
        user2.setFirstName("Alejandro");
        user2.setLastName("Alejandro");
        user2.setPassword("Alejandro");
        entityManager.persist(user2);
        entityManager.flush();


        Game game1 = new Game();
        game1.setWinner(user1);
        game1.setCreationDate(LocalDateTime.now());
        game1.setTieBreak(true);
        entityManager.persist(game1);
        entityManager.flush();

        Game game2 = new Game();
        game2.setCreationDate(LocalDateTime.now());
        game2.setWinner(user1);
        game2.setTieBreak(false);
        entityManager.persist(game2);
        entityManager.flush();

        Game game3 = new Game();
        game3.setCreationDate(LocalDateTime.now());
        game3.setWinner(user2);
        game3.setTieBreak(true);
        entityManager.persist(game3);
        entityManager.flush();

        
        Long victories1 = gameRepository.findTieBreaksByNickname("Antonio");
        Long victories2 = gameRepository.findTieBreaksByNickname("Alejandro");

     
        assertTrue(victories1==1);
        assertTrue(victories2==1);
    }

    @Test
    public void findTotalGamesPlayedByDayTest() {
       
        LocalDateTime date1 = LocalDateTime.of(2022, 2, 1, 2, 2);
        Game game1 = new Game();
        game1.setCreationDate(date1);
        entityManager.persist(game1);
        entityManager.flush();

        LocalDateTime date2 = LocalDateTime.of(2022, 2, 2, 2, 2);
        Game game2 = new Game();
        game2.setCreationDate(date2);
        entityManager.persist(game2);
        entityManager.flush();

        

        
        List<Integer> gamesPlayedByDay = gameRepository.findTotalGamesPlayedByDay();

       
        assertTrue(gamesPlayedByDay.size()>0);
    }

    @Test
    public void findTotalGamesPlayedByUserLobbiesTest() {
        
        Lobby lobby1 = new Lobby();
        lobby1.setCode("password1");
        entityManager.persist(lobby1);
        entityManager.flush();
        
        Lobby lobby2 = new Lobby();
        lobby2.setCode("password2");
        entityManager.persist(lobby2);
        entityManager.flush();
        
        Game game1 = new Game();
        game1.setLobby(lobby1);
        game1.setCreationDate(LocalDateTime.now());
        entityManager.persist(game1);
        entityManager.flush();

        Game game2 = new Game();
        game2.setLobby(lobby1);
        game2.setCreationDate(LocalDateTime.now());
        entityManager.persist(game2);
        entityManager.flush();

        Game game3 = new Game();
        game3.setLobby(lobby2);
        game3.setCreationDate(LocalDateTime.now());
        entityManager.persist(game3);
        entityManager.flush();

        List<Lobby> lobbies = Arrays.asList(lobby1, lobby2);
        
        
        Integer gamesPlayed = gameRepository.findTotalGamesPlayedByUserLobbies(lobbies);

       
        assertTrue(gamesPlayed==3);
    }
    @Test
    public void findLobbiesByDayTest() {
        
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1L);
        
        Lobby lobby1 = new Lobby();
        lobby1.setCode("password1");
        lobbyRepository.save(lobby1);
        
        Lobby lobby2 = new Lobby();
        lobby2.setCode("password2");
        lobbyRepository.save(lobby2);

        Game game1 = new Game();
        game1.setLobby(lobby1);
        game1.setCreationDate(yesterday);
        gameRepository.save(game1);

        Game game2 = new Game();
        game2.setLobby(lobby2);
        game2.setCreationDate(today);
        gameRepository.save(game2);


        
        List<List<Lobby>> lobbiesByDay = gameRepository.findLobbiesByDay();
        
        
        assertFalse(lobbiesByDay.size()==4);
        assertTrue(lobbiesByDay.get(0).get(0).getCode()=="password1");
        assertTrue(lobbiesByDay.get(1).get(0).getCode()=="password2");
    }


    @Test
    public void findLobbiesTest() {
        
        Lobby lobby1 = new Lobby();
        lobby1.setCode("password1");
        lobbyRepository.save(lobby1);
        
        Lobby lobby2 = new Lobby();
        lobby2.setCode("password2");
        lobbyRepository.save(lobby2);
        
        Game game1 = new Game();
        game1.setLobby(lobby1);
        game1.setCreationDate(LocalDateTime.now());
        gameRepository.save(game1);

        Game game2 = new Game();
        game2.setLobby(lobby2);
        game2.setCreationDate(LocalDateTime.now());
        gameRepository.save(game2);
        
        List<Lobby> lobbies = gameRepository.findLobbies();

        
        assertTrue(lobbies.size()!=4);
        assertTrue(lobbies.get(0).getCode()=="password1");
        assertTrue(lobbies.get(1).getCode()=="password2");
    }

    @Test
    public void findGameActiveTest() {
        
        Game game1 = new Game();
        game1.setCreationDate(LocalDateTime.now());
        game1.setActive(true);
        gameRepository.save(game1);

        Game game2 = new Game();
        game2.setCreationDate(LocalDateTime.now());
        game2.setActive(false);
        gameRepository.save(game2);

        
        List<Game> activeGames = gameRepository.findGameActive(true);
        List<Game> inactiveGames = gameRepository.findGameActive(false);

        
        assertTrue(activeGames.size()==1);
        assertFalse(inactiveGames.size()==3);
        assertTrue(activeGames.get(0).isActive());
        assertFalse(inactiveGames.get(0).isActive());
    }
}



