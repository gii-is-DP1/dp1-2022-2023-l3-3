package sevenislands.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.UserType;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.LobbyService;
import sevenislands.lobby.lobbyUser.LobbyUser;
import sevenislands.lobby.lobbyUser.LobbyUserRepository;
import sevenislands.lobby.lobbyUser.LobbyUserService;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    SessionRegistry sessionRegistry;
    @Mock
    LobbyRepository lobbyRepository;
    @Mock
    LobbyUserRepository lobbyUserRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    HttpServletRequest request;


    User user;
    UserService userService;
    List<User> usersRepo = new ArrayList<>();
    LobbyService lobbyService;
    LobbyUserService lobbyUserService;

    @BeforeEach
    public void config() {
        lobbyService = new LobbyService(lobbyRepository);
        lobbyUserService = new LobbyUserService(lobbyUserRepository, lobbyService);
        userService = new UserService(authenticationManager, sessionRegistry,
        passwordEncoder, userRepository, null, lobbyUserService);
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        usersRepo.add(user);
    }
    
    @Test
    public void allUsersFoundSuccessful() {
        when(userRepository.findAll()).thenReturn(usersRepo);
        List<User> users = userService.findAll();
        assertNotNull(users, "El servicio devuelve un objeto nulo");
        assertFalse(users.isEmpty(), "El servicio de vuelve una colección vacia");
        assertEquals(1, users.size(), "El servicio devuelve una colección con un tamaño diferente");
    }
    @Test
    public void saveTestUnsuccessfulDueToExistence() {
        when(userRepository.save(any())).thenThrow(new IllegalArgumentException());

        User newUser2 = userService.createUser(555, "prueba", "prueba@sevenislands.com");
        assertThrows(IllegalArgumentException.class, () -> userService.save(newUser2));
        
    }

    @Test
    public void findByIdTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserById(1).get());
        assertEquals(null, userService.findUserById(2).orElse(null));

    }

    @Test
    public void findByEmailTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        when(userRepository.findByEmail("prueba@sevenislands.com")).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserByEmail("prueba@sevenislands.com"));
        assertEquals(null, userService.findUserByEmail("pruebaMal@sevenislands.com"));

    }

    @Test
    public void findAllTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        UserService userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        when(userRepository.findAll()).thenReturn(usersRepo);
        assertEquals(usersRepo, userService.findAll());
    }

    @Test
    public void checkersTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        when(userRepository.checkUserEmail("prueba@sevenislands.com")).thenReturn(true);
        when(userRepository.checkUserEmail("pruebaFalso@sevenislands.com")).thenReturn(false);

        assertEquals(true, userService.checkUserByEmail("prueba@sevenislands.com"));
        assertEquals(false, userService.checkUserByEmail("pruebaFalso@sevenislands.com"));
    
    }
    
    @Test
    public void updateTest(){
        User oldUser = user.copy();
        userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        user.setPassword("short");
        assertThrows(IllegalArgumentException.class, ()-> userService.updateUser(user, user.getId().toString(), 0));
        user.setPassword("ehrbvuhwerbvuherbv");

        assertThrows(Exception.class, ()-> userService.updateUser(user, user.getId().toString(), 4));

        user.setEmail("novalidEmail");
        assertThrows(IllegalArgumentException.class, ()-> userService.updateUser(user, user.getId().toString(), 0));
        user.setEmail("sergio@gmail.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("jvnipfbvrbkef");
        user = userService.updateUser(user, user.getId().toString(), 0);
        assertNotEquals(oldUser.getEmail(), user.getEmail());
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(user));
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getNickname(), 2).getEmail());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getEmail(), 1).getEmail());
        assertNotEquals(oldUser.getEmail(), userService.updateUser(user, user.getId().toString(), 3).getEmail());
        user.setNickname("player1");
    }

    @Test
    public void addUserTest(){
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        user.setPassword("corta");
        assertThrows(IllegalArgumentException.class, ()-> userService.addUser(user, false, null, null));
        user.setPassword("vbhcrbfhvbrhvbrhttvt");
        user.setEmail("notValidEmail");
        assertThrows(IllegalArgumentException.class, ()-> userService.addUser(user, false, null, null));
        user.setEmail("validEmail@gmail.com");
        assertThrows(Exception.class, ()-> userService.addUser(user, false, null, null));
        
        when(passwordEncoder.encode(any())).thenReturn("jvnipfbvrbkef");
        when(userRepository.save(any())).thenReturn(user);

        user.setPassword("jvnipfbvrbkef");
        User esperado = userService.addUser(user, true, authenticationManager, request);
        assertEquals(user.getEmail(), esperado.getEmail());
        user.setUserType(UserType.admin);
        esperado = userService.addUser(user, true, authenticationManager, request);
        assertEquals(user.getEmail(), esperado.getEmail());
    
    }

    @Test
    public void deleteUserTest() {
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        User user2 = userService.createUser(2, "prueba2", "prueba2@sevenislands.com");

        Lobby lobby = new Lobby();
        lobby.generatorCode();
        lobby.setActive(true);
        LobbyUser lobbyUser=new LobbyUser();
        lobbyUser.setUser(user);
        lobbyUser.setUser(user2);
        assertFalse(userService.deleteUser(2, user));
        when(lobbyUserRepository.findByUser(user)).thenReturn(Optional.of(List.of(lobbyUser)));

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertTrue(userService.deleteUser(2, user));
        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        assertTrue(userService.deleteUser(2, user));
    }
  
    @Test
    public void enableUserTest(){
        userService = new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        user = userService.createUser(1, "prueba", "prueba@sevenislands.com");
        User user2 = userService.createUser(2, "prueba2", "prueba2@sevenislands.com");
        assertTrue(userService.enableUser(2, user2));
        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        assertTrue(userService.enableUser(2, user));
        assertFalse(userService.enableUser(2, user2));
        user2.setEnabled(false);
        assertTrue(userService.enableUser(2, user));
    }

    @Test
    public void findAllUserTest(){
        userService =new UserService(null, null,
        passwordEncoder, userRepository, null, null);
        User user2 = userService.createUser(2, "prueba2", "prueba2@sevenislands.com");
        User user3 = userService.createUser(3, "prueba3", "prueba3@sevenislands.com");
        User user4 = userService.createUser(4, "prueba4", "prueba4@sevenislands.com");
        User user5 = userService.createUser(5, "prueba5", "prueba5@sevenislands.com");
        User user6 = userService.createUser(6, "prueba6", "prueba6@sevenislands.com");
        User user7 = userService.createUser(7, "prueba7", "prueba7@sevenislands.com");
        User user8 = userService.createUser(8, "prueba8", "prueba8@sevenislands.com");
        User user9 = userService.createUser(9, "prueba9", "prueba9@sevenislands.com");
        User user10 = userService.createUser(10, "prueba10", "prueba10@sevenislands.com");
        User user11 = userService.createUser(11, "prueba11", "prueba11@sevenislands.com");
        List<User> users = List.of(user,user2,user3,user4,user5,user6,user7,user8, user9, user10, user11);
        when(userRepository.findAll()).thenReturn(users);
        Pageable page = PageRequest.of(0, 5);
        assertEquals(5, userService.findAllUser(page, 5).getSize());
        page = PageRequest.of(1, 5);
        assertEquals(5, userService.findAllUser(page, 5).getSize());
        page = PageRequest.of(2, 5);
        assertEquals(1, userService.findAllUser(page, 5).getSize());
    }





   
}