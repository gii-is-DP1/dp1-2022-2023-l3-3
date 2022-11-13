package sevenislands.lobby;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import sevenislands.lobby.exceptions.NotExistLobbyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository){
        this.lobbyRepository=lobbyRepository;
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
    
    @Transactional(readOnly = true)
    public long numPartidas() {
        long partidas=lobbyRepository.count();
        return partidas;
    }

    @Transactional
	public void save(Lobby lobby) {
        lobbyRepository.save(lobby);
	}

    @Transactional 
	public void update(Lobby lobby) {
	    lobbyRepository.updatePlayers(lobby, lobby.getId());
	}

    @Transactional(readOnly = true, rollbackFor = NotExistLobbyException.class)
    public Lobby findLobbyByCode(String code) throws NotExistLobbyException {
        Optional<Lobby> lobby = lobbyRepository.findByCode(code);
        if(lobby.isPresent()){
            return lobby.get();
        } else {
            throw new NotExistLobbyException();
        }
    }

    @Transactional(rollbackFor = NotExistLobbyException.class)
    public Lobby findLobbyByPlayer(Integer player_id) {
        return lobbyRepository.findByLobbyId(lobbyRepository.findByPlayer(player_id));
    }

    @Transactional
	public Boolean checkLobbyByCode(String code) {
		return lobbyRepository.checkLobby(code);
	}

    @Transactional
	public Boolean checkUserLobbyByName(Integer id) {
	    return lobbyRepository.findByPlayer(id)!=null;
	}

    /*@Transactional(rollbackFor = NotExistLobbyException.class)
    public void addPlayerToLobby(Integer code, Player player) throws NotExistLobbyException{
        Lobby lobby = getByCode(code);
        lobby.getMembers().add(player);
        lobbyRepository.save(lobby);
    }*/

}