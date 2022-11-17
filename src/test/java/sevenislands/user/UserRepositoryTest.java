package sevenislands.user;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    UserRepository userRepository;

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
        userRepository.save(user1);
        assertNotNull(userRepository.findById(user1.getId()).orElseGet(null));
        user1 = userRepository.findById(user1.getId()).orElseGet(null);
        assertNotEquals(user1.getEmail(), oldEmail);
    }

    
    public void retrieveAllUsersSuccess() {
        List<User> users = userRepository.findAll();
        assertNotNull(users, "El repositorio devuelve una colección nula");
        assertFalse(users.isEmpty(), "El repositorio ha devuelto una colección vacía");
        assertEquals(11, users.size(), "El repositorio ha devuelto una colección incompleta");
    }

    @Test
    public void retrieveUserByNicknameSuccessful() {
        User user = userRepository.findByNickname("admin1").get();
        assertEquals("admin1", user.getNickname(), "El nombre de usuario no es igual a admin1");
    }

    @Test()
    public void retrieveUserByNicknameNotSuccessful(){
        assertThrows(NoSuchElementException.class, () -> {
            userRepository.findByNickname("FalsoAdmin1").get();
        });
    }

    @Test
    public void retrieveUserByEmailSuccessful() {
        User user = userRepository.findByEmail("admin1@sevenislands.com").get();
        assertEquals("admin1@sevenislands.com", user.getEmail());
    }

    @Test()
    public void retrieveUserByEmailNotSuccessful(){
        assertThrows(NoSuchElementException.class, () -> {
            userRepository.findByEmail("notAnEmail").get();
        });
    }

    @Test
    public void checkUserInfoExists() {
        Boolean exists = userRepository.checkUserNickname("admin1");
        assertTrue(exists, "El repositorio no devuelve el usuario admin1");
        Boolean email = userRepository.checkUserEmail("admin1@sevenislands.com");
        assertTrue(email, "El repositorio no devuelve el usuario con email admin1@sevenislands.com"); 
    }

    @Test
    public void retrieveAuthorities() {
        List<String> authorities = userRepository.findAuthorities();
        assertNotNull(authorities, "El repositorio devuelve una colección nula");
        assertFalse(authorities.isEmpty(), "El repositorio ha devuelto una colección vacía");
        assertEquals(2, authorities.size(), "El repositorio ha devuelto una colección de tamaño inesperado");
    }

    @Test
    public void changeUserInfo() {
        User user = userRepository.findByNickname("admin1").get();
        String cambio = "cambiado";
        user.setLastName(cambio);
        userRepository.save(user);

        User newUser = userRepository.findByNickname("admin1").get();
        assertEquals(cambio, newUser.getLastName());
    }
}