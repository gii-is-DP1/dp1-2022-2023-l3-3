package org.springframework.samples.sevenislands.achievement;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
    
    @Autowired
    private AchievementRepository achievementRepository;

    @Transactional
    public Achievement getAchievementById(Integer id){
        return achievementRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Achievement achievement){
        achievementRepository.save(achievement);
    }

    @Transactional
    public Collection<Achievement> getAchievementByType(AchievementType achievementType){
        return achievementRepository.findByType(achievementType);
    }

}
