package sevenislands.user;


import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    public User createUser(Integer id, String nickname,String email) {
        User user = new User();
        user.setId(id);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword("pass");
        user.setEnabled(true);
        user.setFirstName("Prueba");
        user.setLastName("Probando");
        user.setBirthDate(new Date());
        user.setAvatar("resource/images/avatars/playerAvatar.png");
        user.setUserType("player");

        return user;
    }

    @BeforeEach
    public void config() {
        User user = createUser(666, "prueba", "prueba@sevenislands.com");
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);
    }

    // @Test
    // public void saveTestUnsuccessfulDueToExistence() {
    //     UserService userService = new UserService(userRepository);
    //     User newUser = createUser(555, "prueba", "prueba@sevenislands.com");
    //     assertThrows(DataAccessException.class, () -> userService.save(newUser));
    // }

    // @Test
    // public void saveTestSuccessful() {
    //     UserService userService = new UserService(userRepository);
    //     User user = createUser(123, "prueba2", "prueba2@sevenislands.com");
    //     try {
    //         userService.save(user);
    //         verify(userRepository).save(user);
    //     } catch (Exception e) {
    //         fail("No se debe lanzar ninguna excepción");
    //     }
    // }

    
}
