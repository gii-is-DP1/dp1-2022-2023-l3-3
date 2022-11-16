package sevenislands.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    UserRepository userRepository;

    @Test
    public void TestDataAllFindAllSuccess(){
        List<User> user= new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(user::add);

        assertNotNull(user);
        assertFalse(user.isEmpty());
        assertEquals(11, user.size());
    }

    @Test
    public void initialDataAndFindSuccessTest(){
        List<User> players = userRepository.findAll();

        assertNotEquals(0, players.size());
        assertNull(userRepository.findByNickname("sergioFalso").orElse(null));
    }

    @Test
    public void findByIdAndUpdateDataTest(){
        List<User> users = userRepository.findAll();
        User user1 = users.get(0);
        String oldEmail = user1.getEmail();
        user1.setEmail(oldEmail+"mod");
        userRepository.updateUser(user1, user1.getId());
        assertNotNull(userRepository.findById(user1.getId()).orElseGet(null));
        user1 = userRepository.findById(user1.getId()).orElseGet(null);
        assertNotEquals(user1.getEmail(), oldEmail);
        
    }
}
