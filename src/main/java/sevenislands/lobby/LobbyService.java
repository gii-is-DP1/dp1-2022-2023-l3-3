package sevenislands.lobby;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.exceptions.NotExistLobbyException;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository){
        this.lobbyRepository=lobbyRepository;
    }

    /**
     * Crea un código aleatorio para la lobby.
     * @return String
     */
    public String generatorCode() {
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Integer RANDOM_STRING_LENGTH = 8;
        StringBuffer randomString = new StringBuffer();
        
        for(int i = 0; i<RANDOM_STRING_LENGTH; i++) {
            Random randomGenerator = new Random();
            char ch = CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()-1));
            randomString.append(ch);
        }
        return randomString.toString();
    }
    
    @Transactional(readOnly = true)
    public Integer numPartidas() {
        return lobbyRepository.getNumOfLobby();
    }

    @Transactional
	public void save(Lobby lobby) {
        lobbyRepository.save(lobby);
	}

    @Transactional 
	public void update(Lobby lobby) {
	    lobbyRepository.updateLobby(lobby, lobby.getId());
	}

    @Transactional(readOnly = true, rollbackFor = NotExistLobbyException.class)
    public Lobby findLobbyByCode(String code) throws NotExistLobbyException {
        Lobby lobby = lobbyRepository.findByCode(code);
        if(lobby != null){
            return lobby;
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional(rollbackFor = NotExistLobbyException.class)
    public Optional<Lobby> findLobbyByPlayer(Integer userId) {
        return lobbyRepository.findById(lobbyRepository.findLobbyIdByPlayer(userId));
    }

    @Transactional
	public Boolean checkLobbyByCode(String code) {
		return lobbyRepository.checkLobby(code);
	}

    @Transactional
	public Boolean checkUserLobbyByName(Integer id) {
	    return lobbyRepository.findLobbyIdByPlayer(id)!=null;
	}
}