package sevenislands.island;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.card.Card;
import sevenislands.enums.Tipo;
import sevenislands.game.Game;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandRepository;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.user.User;
import sevenislands.user.UserRepository;

@DataJpaTest
public class IslandRepositoryTest {
    
    @Autowired
    IslandRepository islandRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LobbyRepository lobbyRepository;

    @BeforeEach
    public void config(){
        Lobby lobby = new Lobby();
        List<User> users = userRepository.findAll().stream().filter(u -> u.getUserType().equals("player")).limit(3).collect(Collectors.toList());
        lobby.setUsers(users);
        lobby.setCode(lobby.generatorCode());
        lobbyRepository.save(lobby);
        Island island = new Island();
        Card card = new Card();
        Game game = new Game();
        game.setCreationDate(new Date(System.currentTimeMillis()));
        game.setLobby(lobby);
        card.setMultiplicity(4);
        card.setTipo(Tipo.Caliz);
        card.setGame(game);
        island.setGame(game);
        island.setCard(card);
        island.setNum(1);
        island.setId(999);
        islandRepository.save(island);
        
    }

    @Test
    public void getIslandNumberSuccessful() {
        Integer num = islandRepository.getIslandNumberById(2);
        assertEquals(1, num, "La isla debería tener el numero 1");
    }

    @Test
    public void changeIslandInfo() {
        Island island = islandRepository.findById(1).get();
        Integer cambio = 2;
        island.setNum(cambio);
        islandRepository.save(island);

        Island newIsland = islandRepository.findById(1).get();
        assertEquals(cambio, newIsland.getNum());
    }
}
