package sevenislands.lobby;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class LobbyServiceTest {
    
    @Autowired    
    LobbyRepository lobbyRepository;

 
}
