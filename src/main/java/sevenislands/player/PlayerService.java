package sevenislands.player;

import java.sql.Date;
import java.util.Optional;

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
		Boolean flag = playerRepository.findAll().stream()
			.anyMatch(u -> u.getEmail().equals(player.getEmail()) || u.getNickname().equals(player.getNickname()));
		System.out.println("=========================");
		System.out.println(flag);
		if(flag){
			throw new ExistPlayerException("Existe el jugador");
		} else{
			playerRepository.save(player);
			//userService.save(player);
		}
		try {
			playerRepository.save(player);
			userService.save(player);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public Optional<Player> findPlayer(Integer id) throws DataAccessException{
		return playerRepository.findById(id);
	}

	@Transactional
    public Optional<Player> findPlayer(String name) {
        return playerRepository.findByName(name);
    }

	@Transactional 
	public void update(Player player) {
		playerRepository.updatePlayer(player, player.getId());
	}

	@Transactional
	public void save(Player player) throws DataAccessException{
		try {
			playerRepository.save(player);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public void remove(Player player) {
		playerRepository.delete(player);
	}
}