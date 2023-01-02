package sevenislands.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    UserRepository mock;

    User user;
    UserService userService;
    List<User> usersRepo = new ArrayList<>();;

    @BeforeEach
    public void config() {
        userService = new UserService(null, null,
         null, null, mock, null);
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        
        usersRepo.add(user);

    }
    
    @Test
    public void allUsersFoundSuccessful() {
        when(mock.findAll()).thenReturn(usersRepo);
       
        
        List<User> users = userService.findAll();
        assertNotNull(users, "El servicio devuelve un objeto nulo");
        assertFalse(users.isEmpty(), "El servicio de vuelve una colección vacia");
        assertEquals(1, users.size(), "El servicio devuelve una colección con un tamaño diferente");
    }
    @Test
    public void saveTestUnsuccessfulDueToExistence() {
        when(mock.save(any())).thenThrow(new IllegalArgumentException());

        User newUser2 = userService.createUser(555, "prueba", "prueba@sevenislands.com");
        assertThrows(IllegalArgumentException.class, () -> userService.save(newUser2));
        
    }

    @Test
    public void findByIdTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock, null);
        when(mock.findById(1)).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserById(1).get());
        assertEquals(null, userService.findUserById(2).orElse(null));

    }

    @Test
    public void findByEmailTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock, null);
        when(mock.findByEmail("prueba@sevenislands.com")).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserByEmail("prueba@sevenislands.com"));
        assertEquals(null, userService.findUserByEmail("pruebaMal@sevenislands.com"));

    }

    @Test
    public void findAllTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock, null);
        when(mock.findAll()).thenReturn(usersRepo);
        assertEquals(usersRepo, userService.findAll());
    }

    @Test
    public void checkersTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock, null);
        when(mock.checkUserEmail("prueba@sevenislands.com")).thenReturn(true);
        when(mock.checkUserEmail("pruebaFalso@sevenislands.com")).thenReturn(false);

        assertEquals(true, userService.checkUserByEmail("prueba@sevenislands.com"));
        assertEquals(false, userService.checkUserByEmail("pruebaFalso@sevenislands.com"));
    
    }

  





   
}