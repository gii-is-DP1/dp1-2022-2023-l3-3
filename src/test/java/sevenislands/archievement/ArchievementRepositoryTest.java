package sevenislands.archievement;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sevenislands.achievement.Achievement;
import sevenislands.achievement.AchievementRepository;
import sevenislands.enums.AchievementType;
import sevenislands.register.RegisterRepository;

@DataJpaTest
public class ArchievementRepositoryTest {

    @Autowired
    AchievementRepository archivementsRepository;
    
    @Autowired
    RegisterRepository registerRepository;

    @Test
    public void TestAchievement(){
        System.out.println("Hola");
        List<Achievement> archivementsList = archivementsRepository.findByType(AchievementType.Games);
       assertNotEquals(0, archivementsList.size());
    
    }

    @Test
    public void TestPrueba() {
        List<Object []> logros = registerRepository.findArchievementByNickname("player1");
        assertNotEquals(0, logros.size());
    }
    
}
