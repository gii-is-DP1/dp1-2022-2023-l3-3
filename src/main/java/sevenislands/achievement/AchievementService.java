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
    @Transactional(readOnly = true)
    public List<Achievement> getAchievementByType(AchievementType achievementType) {
        return achievementRepository.findByType(achievementType);
    }


	@Transactional(readOnly = true)
	public List<Achievement> findAll(){
		return achievementRepository.findAll();
	}

	@Transactional
    public Achievement updateAchievement(Achievement NewAchievement,Integer id) {
    
	try {
		Achievement oldAchievement=achievementRepository.findById(id).get();
		oldAchievement.setName(NewAchievement.getName());
		oldAchievement.setAchievementType(NewAchievement.getAchievementType());
		oldAchievement.setThreshold(NewAchievement.getThreshold());
		return saveAchievement(oldAchievement);
	} catch (Exception e) {
		throw e;
	}
	

	}

	@Transactional
	public List<Boolean> calculateAchievements(User logedUser) {
		List<Boolean> res = new ArrayList<>();
		IntStream.range(0, 4).forEach(i -> res.add(false));
		Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
		if(game.isPresent()) {
			for(User user: game.get().getLobby().getUsers()) {
				Long totalPoints = gameDetailsService.findPunctuationByNickname(user.getNickname());
				Long totalVictories = gameService.findVictoriesByNickname(user.getNickname());
				Long totalTieBreaks = gameService.findTieBreaksByNickname(user.getNickname());
				Long totalGames = gameDetailsService.findGamesByNickname(user.getNickname());

				for(Achievement achievement: findAll()) {
					if(achievement.getAchievementType().equals(AchievementType.Punctuation) && totalPoints >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						res.set(0, true);
					} else if(achievement.getAchievementType().equals(AchievementType.Victories) && totalVictories >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						res.set(1, true);
					} else if(achievement.getAchievementType().equals(AchievementType.TieBreaker) && totalTieBreaks >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						res.set(2, true);
					} else if(achievement.getAchievementType().equals(AchievementType.Games) && totalGames >= achievement.getThreshold()) {
						registerService.save(achievement, user);
						res.set(3, true);
					}
				}
			}

		}
		return res;
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
			saveAchievement(achievement);
			return achievement;
		} catch (Exception e) {
			throw e;
		}
	}
}
