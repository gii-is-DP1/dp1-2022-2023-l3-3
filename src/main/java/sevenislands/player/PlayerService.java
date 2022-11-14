package sevenislands.player;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import sevenislands.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

	private PlayerRepository playerRepository;	
	private UserService userService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserService userService) {
		this.playerRepository = playerRepository;
		this.userService = userService;
	}	

	@Transactional
	public void saveNewPlayer(Player player) throws DataAccessException {
		player.setEnabled(true);
		player.setAvatar("playerAvatar.png");
		player.setCreationDate(new Date(System.currentTimeMillis()));
		playerRepository.save(player);
		userService.save(player);
	}

	@Transactional(readOnly = true)
	public Player findPlayer(Integer id) throws DataAccessException{
		return playerRepository.findById(id).get();
	}

	@Transactional
    public Player findPlayer(String name) {
        return playerRepository.findByName(name);
    }

	@Transactional 
	public void update(Player player) {
		playerRepository.updatePlayer(player, player.getId());
	}

	@Transactional
	public void save(Player player) {
		playerRepository.save(player);
	}

	@Transactional
	public void remove(Player player) {
		playerRepository.delete(player);
	}
}