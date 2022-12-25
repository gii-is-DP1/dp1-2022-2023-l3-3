package sevenislands.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.crypto.Data;

import org.apache.jasper.tagplugins.jstl.core.When;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    UserRepository mock;

    User user;
    UserService userService;
    List<User> usersRepo = new ArrayList<>();

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void config() {
        userService = new UserService(null, null,
         null, passwordEncoder, mock);
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
        UserService userService = new UserService(null, null,null,passwordEncoder, mock);
        when(mock.findById(1)).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserById(1).get());
        assertEquals(null, userService.findUserById(2).orElse(null));

    }

    @Test
    public void findByEmailTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock);
        when(mock.findByEmail("prueba@sevenislands.com")).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserByEmail("prueba@sevenislands.com"));
        assertEquals(null, userService.findUserByEmail("pruebaMal@sevenislands.com"));

    }

    @Test
    public void findAllTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,null,null, mock);
        when(mock.findAll()).thenReturn(usersRepo);
        assertEquals(usersRepo, userService.findAll());
    }

    @Test
    public void checkersTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        userService = new UserService(null, null,null,null, mock);
        when(mock.checkUserEmail("prueba@sevenislands.com")).thenReturn(true);
        when(mock.checkUserEmail("pruebaFalso@sevenislands.com")).thenReturn(false);

        assertEquals(true, userService.checkUserByEmail("prueba@sevenislands.com"));
        assertEquals(false, userService.checkUserByEmail("pruebaFalso@sevenislands.com"));
    
    }
    
    @Test
    public void updateTest(){
        User oldUser = user.copy();
        userService = new UserService(null, null,null,passwordEncoder, mock);
        user.setPassword("short");
        assertThrows(IllegalArgumentException.class, ()-> userService.updateUser(user, user.getId().toString(), 0));
        user.setPassword("ehrbvuhwerbvuherbv");

        assertThrows(Exception.class, ()-> userService.updateUser(user, user.getId().toString(), 4));

        user.setEmail("novalidEmail");
        assertThrows(IllegalArgumentException.class, ()-> userService.updateUser(user, user.getId().toString(), 0));
        user.setEmail("sergio@gmail.com");
        when(mock.findById(user.getId())).thenReturn(Optional.of(user));
        when(mock.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("jvnipfbvrbkef");
        user = userService.updateUser(user, user.getId().toString(), 0);
        assertNotEquals(oldUser.getEmail(), user.getEmail());
        when(mock.findByNickname(any())).thenReturn(Optional.of(user));
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getNickname(), 2).getEmail());
        when(mock.findByEmail(any())).thenReturn(Optional.of(user));
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getEmail(), 1).getEmail());
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getId().toString(), 3).getEmail());
        user.setNickname("player1");



        
    }
  





   
}