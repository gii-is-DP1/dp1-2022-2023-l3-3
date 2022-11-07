package org.springframework.samples.sevenislands.lobby;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.samples.sevenislands.player.Player;
import org.springframework.stereotype.Service;


@Service
public class LobbyService {
    
    private LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository LobbyRepository){
        this.lobbyRepository=LobbyRepository;
    }

   //creacion del codigo de la lobby
    public String generatorCode() {
        Integer codigo=0;
		String code="";
		for (int j = 0; j < 8; j++) {
			Integer random=(int)(Math.random()*10+1);
			code+=random.toString();
		}
		codigo=Integer.parseInt(code);
        return code;
    }
    
    @Transactional
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

    @Transactional
    public Lobby findLobbyByCode(String code) {
        return lobbyRepository.findByCode(code);
    }

    @Transactional
    public Lobby findLobbyByPlayer(Integer player_id) {
        return lobbyRepository.findByLobbyId(lobbyRepository.findByPlayer(player_id));
    }

}