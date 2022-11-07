package org.springframework.samples.sevenislands.lobby;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.lobby.LobbyExceptions.NotExistLobbyException;
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
    public Integer generatorCode(){
        Integer codigo=0;
		String code="";
		for (int j = 0; j < 7; j++) {
			Integer random=(int)(Math.random()*10+1);
			code+=random.toString();
		}
		codigo=Integer.parseInt(code);
        return codigo;
    }
    
    @Transactional(readOnly = true)
    public long numPartidas(){
        long partidas=lobbyRepository.count();
        return partidas;
    }

    @Transactional 
	public void save(Lobby r) {
	    lobbyRepository.save(r);
	}

    @Transactional(readOnly = true)
    public Lobby getByCode(Integer code){
        return lobbyRepository.findByCode(code).orElse(null);
    }

    @Transactional(rollbackFor = NotExistLobbyException.class)
    public void addPlayerToLobby(Integer code, Player player) throws NotExistLobbyException{
        Lobby lobby = getByCode(code);
        if(lobby != null){
            lobby.getMembers().add(player);
            lobbyRepository.save(lobby);
        } else {
            throw new NotExistLobbyException();
        }
    }
}