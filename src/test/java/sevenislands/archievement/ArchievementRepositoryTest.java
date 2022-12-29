package sevenislands.archievement;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.achievement.Achievement;
import sevenislands.achievement.AchievementRepository;
import sevenislands.enums.AchievementType;

@DataJpaTest
public class ArchievementRepositoryTest {

    @Autowired
    AchievementRepository archivementsRepository;

    @Test
    public void TestAchievement(){
        System.out.println("Hola");
        List<Achievement> archivementsList = archivementsRepository.findByType(AchievementType.Games);
       assertNotEquals(0, archivementsList.size());
    
    }
    
}
