package sevenislands.achievement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.AchievementType;
import sevenislands.enums.Mode;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;

@Service
public class AchievementService {
 
    private AchievementRepository achievementRepository;
	private GameService gameService;
	private GameDetailsService gameDetailsService;
	public RegisterService registerService;
	public LobbyUserService lobbyUserService;

    @Autowired
	public AchievementService(AchievementRepository achievementRepository, GameService gameService, GameDetailsService gameDetailsService, 
	RegisterService registerService, LobbyUserService lobbyUserService) {
		this.achievementRepository = achievementRepository;
		this.gameService = gameService;
		this.gameDetailsService = gameDetailsService;
		this.registerService = registerService;
		this.lobbyUserService = lobbyUserService;
	}		

	/**
	 * Encuentra todos los logros de la base de datos.
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)	
	public List<Achievement> findAchievements() throws DataAccessException {
		return (List<Achievement>) achievementRepository.findAll();
	}

	/**
	 * Encuentra un logro dado su id
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
    @Transactional(readOnly = true)
	public Optional<Achievement> findAchievementById(Integer id) throws DataAccessException {
		return achievementRepository.findById(id);
	}

	/**
	 * Guarda o actualiza el logro.
	 * @param achievement
	 * @throws DataAccessException
	 */
    @Transactional
	public Achievement saveAchievement(Achievement achievement) throws DataAccessException {
		return achievementRepository.save(achievement);
	}
	
	/**
	 * Encuentra todos lo logros de un mismo tipo.
	 * @param achievementType
	 * @return
	 */
    @Transactional
    public Collection<Achievement> getAchievementByType(AchievementType achievementType) {
        return achievementRepository.findByType(achievementType);
    }


	@Transactional
	public List<Achievement> findAll(){
		return StreamSupport.stream(achievementRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Transactional
    public Achievement updateAchievement(Achievement NewAchievement, Integer id) {
    
	try {
		Achievement oldAchievement=achievementRepository.findById(Integer.valueOf(id)).get();
		oldAchievement.setName(NewAchievement.getName());
		oldAchievement.setAchievementType(NewAchievement.getAchievementType());
		oldAchievement.setThreshold(NewAchievement.getThreshold());
		return saveAchievement(oldAchievement);
	} catch (Exception e) {
		throw e;
	}
	

	}

	@Transactional
	public List<Achievement> getAll() {
		return (List<Achievement>) achievementRepository.findAll();
	}

	@Transactional
	public List<Boolean> calculateAchievements(User logedUser) throws NotExistLobbyException {
		Optional<Game> game = gameService.findGameByUser(logedUser);
		List<Boolean> result = new ArrayList<Boolean>();
		IntStream.range(0, 4).forEach(i -> result.add(false));
		if(game.isPresent()) {
			List<User> users = lobbyUserService.findUsersByLobbyAndMode(game.get().getLobby(), Mode.PLAYER);
			for(User user: users) {
				Long totalPoints = gameDetailsService.findPunctuationByNickname(user.getNickname());
				Long totalVictories = gameService.findVictoriesByNickname(user.getNickname());
				Long totalTieBreaks = gameService.findTieBreaksByNickname(user.getNickname());
				Long totalGames = gameDetailsService.findGamesByUser(user);

				for(Achievement achievement: getAll()) {
					if(achievement.getAchievementType().equals(AchievementType.Punctuation) && totalPoints >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						result.set(0, true);
					} else if(achievement.getAchievementType().equals(AchievementType.Victories) && totalVictories >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						result.set(1, true);
					} else if(achievement.getAchievementType().equals(AchievementType.TieBreaker) && totalTieBreaks >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						result.set(2, true);
					} else if(achievement.getAchievementType().equals(AchievementType.Games) && totalGames >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						result.set(3, true);
					}
				}
			}
		}
		return result;
	}

	@Transactional
	public Achievement addAchievement(Achievement achievement){
		try {
			if(achievement.getAchievementType().equals(AchievementType.Games)){
				achievement.setDescription("Juega mas de LIMIT partidas");
				achievement.setBadgeImage("logroJugarGames.png");
			}else if(achievement.getAchievementType().equals(AchievementType.Punctuation)){
				achievement.setDescription("Consigue LIMIT puntos");
				achievement.setBadgeImage("logroJugarGames.png");
			}else if(achievement.getAchievementType().equals(AchievementType.TieBreaker)){
				achievement.setDescription("Desempata LIMIT partidas");
				achievement.setBadgeImage("logroTieBreaker.png");
			}else if(achievement.getAchievementType().equals(AchievementType.Victories)){
				achievement.setDescription("Gana mas de LIMIT partidas");
				achievement.setBadgeImage("logroVictories.png");
			}
			return saveAchievement(achievement);
		} catch (Exception e) {
			throw e;
		}
	}
}
