package sevenislands.player;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    List<Player> playerList = new ArrayList<>();

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void config(){
        UserService userService = new UserService(userRepository);
        PlayerService playerService = new PlayerService(playerRepository, userService);
        Player player1 = new Player();
        player1.setAvatar("resource/images/avatars/playerAvatar.png");
        player1.setBirthDate(Date.valueOf("2002-04-03"));
        player1.setEmail("falseEmail@gmail.com");
        player1.setEnabled(true);
        player1.setFirstName("Sergio");
        player1.setLastName("Santiago");
        player1.setNickname("nickFalsoPrueba");
        player1.setPassword("password");
        player1.setId(123456789);
        
        

        when(playerRepository.findAll()).thenReturn(playerList);
        Optional<Player> playerOpt = Optional.of(player1);
        //when(playerRepository.findById(123456789)).thenReturn(playerOpt);
        playerService.saveNewPlayer(player1);
    }


    @Test
    public void saveTestUnsucefull(){
        List<Player> playerList = playerRepository.findAll();
        UserService userService = new UserService(userRepository);
        PlayerService playerService = new PlayerService(playerRepository, userService);
        Player player1 = new Player();
        player1.setAvatar("resource/images/avatars/playerAvatar.png");
        player1.setBirthDate(Date.valueOf("2002-04-03"));
        player1.setEmail("falseEmail@gmail.com");
        player1.setEnabled(true);
        player1.setFirstName("Sergio");
        player1.setLastName("Santiago");
        player1.setNickname("nickFalsoPrueba");
        player1.setPassword("password");
        player1.setId(1236789);
        playerList.add(player1);

        playerList = playerRepository.findAll();
        assertThrows(ExistPlayerException.class, ()-> playerService.saveNewPlayer(player1));
    }


    
}
