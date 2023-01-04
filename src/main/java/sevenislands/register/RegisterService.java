package sevenislands.register;

import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.achievement.Achievement;
import sevenislands.user.User;

@Service
public class RegisterService {
    
    private RegisterRepository registerRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Transactional
    public List<Object []> findRegistersByNickname(String nickname) {
        return registerRepository.findArchievementByNickname(nickname);
    }

    @Transactional
    public List<Object []> countAchievements() {
        return registerRepository.countAchievements();
    }

    @Transactional
    public void save(Achievement achievement, User user) {
        Register register = new Register();
        register.setAchievement(achievement);
        register.setUser(user);
        register.setAcquisitionDate(new Date(System.currentTimeMillis()));
        registerRepository.save(register);
    }

}
