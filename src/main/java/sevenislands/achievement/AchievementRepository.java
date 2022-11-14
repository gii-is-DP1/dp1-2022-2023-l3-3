package sevenislands.achievement;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import sevenislands.enums.AchievementType;


public interface AchievementRepository extends CrudRepository<Achievement, Integer>{

    /**
     * Encuentra todos los logros de un tipo dado.
     * @param achievementType
     * @return
     */
    @Query("SELECT achievement FROM Achievement achievement WHERE achievement.achievementType=:achievementType")
    Collection<Achievement> findByType(@Param("achievementType") AchievementType achievementType);
}
