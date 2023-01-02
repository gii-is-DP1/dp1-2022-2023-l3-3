package sevenislands.achievement;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

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
	public List<Achievement> findAll(){
		return StreamSupport.stream(achievementRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Transactional
    public void updateAchievement(Achievement NewAchievement,String id) {
    
	try {
		Achievement oldAchievement=achievementRepository.findById(Integer.valueOf(id)).get();
		oldAchievement.setName(NewAchievement.getName());
		oldAchievement.setAchievementType(NewAchievement.getAchievementType());
		oldAchievement.setThreshold(NewAchievement.getThreshold());
		saveAchievement(oldAchievement);
	} catch (Exception e) {
		throw e;
	}
	

	}
}
