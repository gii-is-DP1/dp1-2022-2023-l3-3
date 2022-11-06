package org.springframework.samples.sevenislands.lobby;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LobbyService {
    
    private LobbyRepository LobbyRepository;

    @Autowired
    public LobbyService(LobbyRepository LobbyRepository){
        this.LobbyRepository=LobbyRepository;
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
    
    @Transactional
    public long numPartidas(){
        long partidas=LobbyRepository.count();
        return partidas;
    }

    @Transactional 
	public void save(Lobby r) {
	    LobbyRepository.save(r);
	}
}