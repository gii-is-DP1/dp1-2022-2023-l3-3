package org.springframework.samples.sevenislands.lobby;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.samples.sevenislands.lobby.lobbyExceptions.noExistPlayerException;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository LobbyRepository){
        this.lobbyRepository=LobbyRepository;
    }

   //creacion del codigo de la lobby
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
    
    @Transactional
    public long numPartidas() {
        long partidas=lobbyRepository.count();
        return partidas;
    }

    @Transactional
	public void save(Lobby lobby) {
	    //lobbyRepository.save(lobby);
        lobbyRepository.save(lobby);
	}

    @Transactional 
	public void update(Lobby lobby) {
	    lobbyRepository.updatePlayers(lobby, lobby.getId());
	}

    @Transactional 
	public void delete(Lobby lobby) {
	    lobbyRepository.delete(lobby);
	}

    @Transactional
    public Lobby findLobbyByCode(String code) {
        return lobbyRepository.findByCode(code);
    }

    @Transactional(rollbackFor = noExistPlayerException.class)
    public Lobby findLobbyByPlayer(Integer player_id) {
        return lobbyRepository.findByLobbyId(lobbyRepository.findByPlayer(player_id));
    }

}