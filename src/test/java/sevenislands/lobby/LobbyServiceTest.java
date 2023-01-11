package sevenislands.lobby;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.exceptions.NotExistLobbyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    @Test
    @Transactional
    public void testSave() {
        
        Lobby lobby = new Lobby();
        lobby.setId(20);
        lobby.setCode("password1");
        when(lobbyRepository.save(lobby)).thenReturn(lobby);

        
        Lobby savedLobby = lobbyService.save(lobby);

        
        assertEquals(20, savedLobby.getId());
        assertEquals("password1", savedLobby.getCode());
    }

    @Test
    public void testFindAll() {
        
        List<Lobby> expectedLobbies = Arrays.asList(new Lobby(), new Lobby());
        when(lobbyRepository.findAll()).thenReturn(expectedLobbies);
        
        List<Lobby> resultLobbies = lobbyService.findAll();
        
        assertEquals(expectedLobbies, resultLobbies);
    }

    @Test
    public void testFindLobbyByCode_Success() throws NotExistLobbyException {
        
        String code = "12345";
        Lobby expectedLobby = new Lobby();
        when(lobbyRepository.findByCode(code)).thenReturn(Optional.of(expectedLobby));
        
        Lobby resultLobby = lobbyService.findLobbyByCode(code);
       
        assertEquals(expectedLobby, resultLobby);
    }
    
    @Test
    public void testFindLobbyByCode_NotExist() {
        
        String code = "98765";
        when(lobbyRepository.findByCode(code)).thenReturn(Optional.empty());
        
        Executable codeUnderTest = () -> lobbyService.findLobbyByCode(code);
        
        assertThrows(NotExistLobbyException.class, codeUnderTest);
    }

    @Test
    public void testCreateLobbyEntity() {
        
        Lobby expectedLobby = new Lobby();
        expectedLobby.setCode("12345");
        expectedLobby.setActive(true);
        when(lobbyRepository.save(any())).thenReturn(expectedLobby);
        
        Lobby resultLobby = lobbyService.createLobbyEntity();
       
        assertEquals(expectedLobby, resultLobby);
        assertEquals(expectedLobby.getCode(),resultLobby.getCode());
        assertTrue(resultLobby.isActive());
    }

    @Test
    public void testDisableLobby() {
        
        Lobby lobby = new Lobby();
        lobby.setActive(true);
        when(lobbyRepository.save(lobby)).thenReturn(lobby);
        
        Lobby resultLobby = lobbyService.disableLobby(lobby);
        
        assertEquals(lobby, resultLobby);
        assertFalse(resultLobby.isActive());
    }

}