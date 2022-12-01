package sevenislands.achievement;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.enums.AchievementType;

@Service
public class AchievementService {
 
    private AchievementRepository achievementRepository;

    @Autowired
	public AchievementService(AchievementRepository achievementRepository) {
		this.achievementRepository = achievementRepository;
	}		

	/**
	 * Encuentra todos los logros de la base de datos.
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)	
	public Iterable<Achievement> findAchievements() throws DataAccessException {
		return achievementRepository.findAll();
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

}
