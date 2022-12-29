package sevenislands.register;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
