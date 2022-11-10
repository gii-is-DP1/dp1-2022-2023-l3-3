package org.springframework.samples.sevenislands.player;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.sevenislands.user.AuthoritiesService;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

	private PlayerRepository playerRepository;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}	

	@Transactional
	public void save(Player player) throws DataAccessException {
		player.setEnabled(true);
		player.setAvatar("playerAvatar.png");
		player.setCreationDate(new Date(System.currentTimeMillis()));
		//creating player
		playerRepository.save(player);
		//creating user
		userService.saveUser(player);
		//creating authorities
		authoritiesService.saveAuthorities(player.getNickname(), "player");
	}

	@Transactional
    public Player findPlayersByName(String name) {
        return playerRepository.findByName(name);
    }
	
	@Transactional
    public Player findPlayersById(Integer id) {
        return playerRepository.findById(id);
    }

	@Transactional 
	public void update(Player player) {
	    playerRepository.updatePlayer(player, player.getId());
	}
}
//