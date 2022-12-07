package sevenislands.achievement;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sevenislands.enums.AchievementType;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer>{

    /**
     * Encuentra todos los logros de un tipo dado.
     * @param achievementType
     */
    @Query("SELECT achievement FROM Achievement achievement WHERE achievement.achievementType=:achievementType")
    List<Achievement> findByType(@Param("achievementType") AchievementType achievementType);
}
