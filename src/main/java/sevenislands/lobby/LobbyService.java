package sevenislands.lobby;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.exceptions.NotExistLobbyException;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository=lobbyRepository;
    }

    /**
     * Crea un código aleatorio para la lobby.
     * @return String
     */
    /*public String generatorCode() {
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Integer RANDOM_STRING_LENGTH = 8;
        StringBuffer randomString = new StringBuffer();
        
        for(int i = 0; i<RANDOM_STRING_LENGTH; i++) {
            Random randomGenerator = new Random();
            char ch = CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()));
            randomString.append(ch);
        }
        return randomString.toString();
    }*/

    @Transactional
	public Lobby save(Lobby lobby) {
        return lobbyRepository.save(lobby);
	}

    @Transactional
    public List<Lobby> findAll() {
        return lobbyRepository.findAll();
    }

    @Transactional
    public Lobby findLobbyByCode(String code) throws NotExistLobbyException {
        Optional<Lobby> lobby = lobbyRepository.findByCode(code);
        if(lobby.isPresent() && lobby != null){
            return lobby.get();
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional
    public Lobby createLobbyEntity() {
		Lobby lobby = new Lobby();
		lobby.generatorCode();
		lobby.setActive(true);
        return save(lobby);
    }

    @Transactional
    public Lobby disableLobby(Lobby lobby) {
        lobby.setActive(false);
        return lobbyRepository.save(lobby);
    }
}