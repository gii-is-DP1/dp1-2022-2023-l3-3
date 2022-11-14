package sevenislands.achievement;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sevenislands.enums.AchievementType;


public interface AchievementRepository extends CrudRepository<Achievement, Integer>{

    @Query("Select achievement from Achievement achievement where achievement.achievementType = :achievementType")
    Collection<Achievement> findByType(@Param("achievementType") AchievementType achievementType);

    
}
