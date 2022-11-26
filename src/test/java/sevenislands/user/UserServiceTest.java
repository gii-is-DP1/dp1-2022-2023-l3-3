package sevenislands.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    UserRepository mock;

    User user;
    List<User> usersRepo = new ArrayList<>();;

    private User createUser(Integer id, String nickname,String email) {
        user = new User();
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
        user = createUser(1, "prueba", "prueba@sevenislands.com");
        
        usersRepo.add(user);

    }
    
    @Test
    public void allUsersFoundSuccessful() {
        when(mock.findAll()).thenReturn(usersRepo);
       
        UserService userService = new UserService(null,
         null, null, mock);
        List<User> users = userService.findAll();
        assertNotNull(users, "El servicio devuelve un objeto nulo");
        assertFalse(users.isEmpty(), "El servicio de vuelve una colección vacia");
        assertEquals(1, users.size(), "El servicio devuelve una colección con un tamaño diferente");
    }
    @Test
    public void saveTestUnsuccessfulDueToExistence() {
        when(mock.save(any())).thenThrow(new IllegalArgumentException());
        UserService userService = new UserService(null,null,null, mock);
        //userService.save(newUser);
        User newUser2 = createUser(555, "prueba", "prueba@sevenislands.com");
        assertThrows(IllegalArgumentException.class, () -> userService.save(newUser2));
        
    }

   
}