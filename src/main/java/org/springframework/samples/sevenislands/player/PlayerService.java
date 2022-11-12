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
	private UserService userService;
	private AuthoritiesService authoritiesService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserService userService, AuthoritiesService authoritiesService) {
		this.playerRepository = playerRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}	

	@Transactional
	public void saveNewPlayer(Player player) throws DataAccessException {
		player.setEnabled(true);
		player.setAvatar("playerAvatar.png");
		player.setCreationDate(new Date(System.currentTimeMillis()));
		//creating player
		playerRepository.save(player);
		//creating user
		userService.saveUser(player);
		//creating authorities
		authoritiesService.saveAuthorities(player.getId(), "player");
	}

	@Transactional(readOnly = true)
	public Player findPlayerById(Integer id) throws DataAccessException{
		return playerRepository.findById(id).get();
	}

	@Transactional
    public Player findPlayersByName(String name) {
        return playerRepository.findByName(name);
    }
	
	@Transactional
    public Player findPlayersById(Integer id) {
        return playerRepository.findById(id).get();
    }

	@Transactional 
	public void update(Player player) {
		//creating player
		playerRepository.updatePlayer(player, player.getId());
		//creating user
		//userService.update(player);
		//creating authorities
		//authoritiesService.saveAuthorities(player.getId(), "player");
	}

	@Transactional
	public void save(Player player) {
		playerRepository.save(player);
	}
}