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

	@Transactional(readOnly = true)	
	public Iterable<Achievement> findAchievements() throws DataAccessException {
		return achievementRepository.findAll();
	}

    @Transactional(readOnly = true)
	public Optional<Achievement> findAchievementById(int id) throws DataAccessException {
		return achievementRepository.findById(id);
	}

    @Transactional
	public void saveAchievement(Achievement achievement) throws DataAccessException {
		achievementRepository.save(achievement);
	}
    
    @Transactional
    public Collection<Achievement> getAchievementByType(AchievementType achievementType) {
        return achievementRepository.findByType(achievementType);
    }

}
