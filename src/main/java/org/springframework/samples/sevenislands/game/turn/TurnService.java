package org.springframework.samples.sevenislands.game.turn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnService {
    
    private TurnRepository turnRepository;	
    
    @Autowired
	public TurnService(TurnRepository turnRepository) {
		this.turnRepository = turnRepository;
	}
    
    @Transactional
    public void save(Turn turn){
        turnRepository.save(turn);
    }

    
    @Transactional
    public void update(Turn turn,Integer turn_id){
        turnRepository.updatePlayers(turn,turn_id);
    }
}
