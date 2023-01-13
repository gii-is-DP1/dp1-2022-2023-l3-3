package sevenislands.round;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.card.CardRepository;
import sevenislands.enums.Mode;
import sevenislands.game.Game;
import sevenislands.game.GameRepository;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundRepository;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.user.User;
import sevenislands.user.UserRepository;

@DataJpaTest
public class RoundRepositoryTest {
    
    @Autowired
    RoundRepository roundRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LobbyRepository lobbyRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    LobbyUserRepository lobbyUserRepository;

    Game game;

    @BeforeEach
    public void config(){
        try {
            Lobby lobby = new Lobby();
            List<User> users = userRepository.findAll().stream().filter(u -> u.getUserType().equals("player")).limit(3).collect(Collectors.toList());
            for (User user : users) {
                LobbyUser lobbyUser = new LobbyUser();
                lobbyUser.setLobby(lobby);
                lobbyUser.setUser(user);
                lobbyUser.setMode(Mode.PLAYER);
                lobbyUserRepository.save(lobbyUser);
            }

            lobby.setCode(lobby.generatorCode());
            lobby.setActive(true);
            Lobby lobby2 = lobbyRepository.save(lobby);
            Game game = new Game();
            game.setActive(true);
            game.setId(99);
            game.setCreationDate(LocalDateTime.now());
            game.setLobby(lobby2);
            game = gameRepository.save(game);
            this.game = game;
            Round round = new Round();
            round.setGame(game);
            roundRepository.save(round);
        } catch (Exception e) {
           System.out.println(e);
        }
    }

    @Test
    public void TestDataAllFindAllSuccess(){
        List<Round> round = roundRepository.findAll();
        assertNotNull(round);
        assertFalse(round.isEmpty());
        assertNotEquals(0, round.size());
    }

    @Test
    public void TestFindRoundByGameId(){
        Game game=new Game();
        game.setId(1);
        List<Round> round=roundRepository.findRoundsByGame(game);

        assertNotNull(round);
        assertFalse(round.isEmpty());
        assertEquals(1, round.size());
    }
    
}
