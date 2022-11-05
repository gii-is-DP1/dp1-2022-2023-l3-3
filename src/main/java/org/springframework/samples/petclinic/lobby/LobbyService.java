package org.springframework.samples.petclinic.lobby;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Transactional
    Long getLobbyCount(){
        return lobbyRepository.count();
    }

    @Transactional
    Collection<Lobby> getActiveLobbies(Integer active){
        return lobbyRepository.findByActive(active);
    }
}
