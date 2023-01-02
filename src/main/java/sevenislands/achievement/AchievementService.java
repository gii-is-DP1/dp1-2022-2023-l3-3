package sevenislands.achievement;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.AchievementType;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.register.RegisterService;
import sevenislands.user.User;

@Service
public class AchievementService {
 
    private AchievementRepository achievementRepository;

	private GameService gameService;
	private GameDetailsService gameDetailsService;
	public RegisterService registerService;

    @Autowired
	public AchievementService(AchievementRepository achievementRepository, GameService gameService, GameDetailsService gameDetailsService, RegisterService registerService) {
		this.achievementRepository = achievementRepository;
		this.gameService = gameService;
		this.gameDetailsService = gameDetailsService;
		this.registerService = registerService;
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
	public void saveAchievement(Achievement achievement) throws DataAccessException {
		achievementRepository.save(achievement);
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
	public List<Achievement> getAll() {
		return (List<Achievement>) achievementRepository.findAll();
	}

	@Transactional
	public void calculateAchievements(User logedUser) {
		Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
		if(game.isPresent()) {
			for(User user: game.get().getLobby().getUsers()) {
				Long totalPoints = gameDetailsService.findPunctuationByNickname(user.getNickname());
				Long totalVictories = gameDetailsService.findVictoriesByNickname(user.getNickname());
				Long totalTieBreaks = gameDetailsService.findTieBreaksByNickname(user.getNickname());
				Long totalGames = gameDetailsService.findGamesByNickname(user.getNickname());

				for(Achievement achievement: getAll()) {
					if(achievement.getAchievementType().equals(AchievementType.Punctuation) && totalPoints >= achievement.getThreshold()) {
						registerService.save(achievement, user);
					} else if(achievement.getAchievementType().equals(AchievementType.Victories) && totalVictories >= achievement.getThreshold()) {
						registerService.save(achievement, user);
					} else if(achievement.getAchievementType().equals(AchievementType.TieBreaker) && totalTieBreaks >= achievement.getThreshold()) {
						registerService.save(achievement, user);
					} else if(achievement.getAchievementType().equals(AchievementType.Games) && totalGames >= achievement.getThreshold()) {
						registerService.save(achievement, user);
					}
				}
			}

		}
	}
}
