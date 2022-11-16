package sevenislands.lobby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class LobbyServiceTest {
    
    @Mock
    LobbyRepository lobbyRepository;
    
    @BeforeEach
    public void config(){
        
    }
}
