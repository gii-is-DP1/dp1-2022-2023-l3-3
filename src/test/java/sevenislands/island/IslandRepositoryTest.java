package sevenislands.island;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.card.Card;
import sevenislands.enums.Mode;
import sevenislands.enums.Tipo;
import sevenislands.game.Game;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandRepository;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@DataJpaTest
public class IslandRepositoryTest {
    
    @Autowired
    IslandRepository islandRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LobbyRepository lobbyRepository;
    @Autowired
    LobbyUserRepository lobbyUserRepository;

    UserService userService;

    Island islandTest;

    @BeforeEach
    public void config(){
        userService = new UserService(null, null, null, userRepository, null, null);
        userService = new UserService(null, null, null, userRepository, null, null);
        Lobby lobby = new Lobby();
        IntStream.range(0, 3).forEach(i -> {
            userRepository.save(userService.createUser(10000+i, "playerTest"+i, "EmailTest"+i+"@gmail.com"));
        });
        List<User> users = userRepository.findAll().stream().filter(u -> u.getNickname().contains("Test")).limit(3).collect(Collectors.toList());
        for (User user : users) {
            LobbyUser lobbyUser = new LobbyUser();
            lobbyUser.setLobby(lobby);
            lobbyUser.setUser(user);
            lobbyUser.setMode(Mode.PLAYER);
            lobbyUserRepository.save(lobbyUser);
        }
        lobby.setCode(lobby.generatorCode());
        lobbyRepository.save(lobby);
        Island island = new Island();
        Card card = new Card();
        Game game = new Game();
        game.setCreationDate(LocalDateTime.now());
        game.setLobby(lobby);
        card.setMultiplicity(4);
        card.setTipo(Tipo.Caliz);
        card.setGame(game);
        island.setGame(game);
        island.setCard(card);
        island.setNum(1);
        islandRepository.save(island);
        islandTest = islandRepository.findAll().stream().filter(i -> i.getGame().getLobby().getCode().equals(island.getGame().getLobby().getCode())).findFirst().orElse(null);
    }

    @Test
    public void getIslandNumberSuccessful() {
        Integer num = islandRepository.getIslandNumberById(islandTest.getId());
        assertEquals(islandTest.getNum(), num, "La isla debería tener el numero 1");
    }

    @Test
    public void changeIslandInfo() {
        Island island = islandRepository.findById(islandTest.getId()).get();
        Integer cambio = 2;
        island.setNum(cambio);
        islandRepository.save(island);

        Island newIsland = islandRepository.findById(islandTest.getId()).get();
        assertEquals(cambio, newIsland.getNum());
    }
}
