package sevenislands.user;


import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.validation.BindingResult;

import sevenislands.achievement.Achievement;
import sevenislands.configuration.SecurityConfiguration;
import sevenislands.enums.AchievementType;
import sevenislands.enums.UserType;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.LobbyService;
import sevenislands.register.RegisterService;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
	private MockMvc controller;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
	private SessionRegistry sessionRegistry;

    @MockBean
	private LobbyService lobbyService;

    @MockBean
	private AuthenticationManager authenticationManager;

    @MockBean
	private GameService gameService;

    @MockBean
    private RegisterService registerService;

    @MockBean
	private GameDetailsService gameDetailsService;

    @MockBean
	private TurnService turnService;

    private User userController;
    private Achievement achievement;


    private static final String VIEWS_PLAYER_UPDATE_FORM = "views/updateUserForm";

    @BeforeEach
    public void config(){
        userController = new User();
        userController.setId(1);
        userController.setNickname("user1");
        userController.setPassword("newPassword");
        userController.setEmail("user1@email.com");
        userController.setUserType(UserType.admin);
        userController.setEnabled(true);

        achievement = new Achievement();
        achievement.setId(1);
        achievement.setThreshold(100);
        achievement.setBadgeImage("null");
        achievement.setDescription("null");
        achievement.setName("Name");
        achievement.setAchievementType(AchievementType.Games);
    }

    @WithMockUser(value = "spring")
    @Test
    public void testInitUpdateOwnerForm() throws Exception {
        
        given(userService.checkUser(any(), eq(userController))).willReturn(false);
        controller.perform(get("/settings", userController))
        .andExpect(status().is3xxRedirection());

    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerFormValidUserLoginSuccessful() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getSession()).willReturn(mock(HttpSession.class));
        given(request.getSession().getAttribute("logedUser")).willReturn(userController);
        BindingResult result = mock(BindingResult.class);
        given(result.hasErrors()).willReturn(false);
        given(userService.checkUser(any(), eq(userController))).willReturn(true);
    
        controller.perform(get("/settings").flashAttr("logedUser", userController))
        .andExpect(status().isOk()).andExpect(view().name(VIEWS_PLAYER_UPDATE_FORM));
    
    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);


        given(userService.updateUser(any(), any(), any())).willThrow(new IllegalArgumentException("PUBLIC.USER(EMAIL)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
        atr.put("logedUser", userController);

        controller.perform(post("/settings")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("views/updateUserForm"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewNickname() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        User logedUser = new User();
        logedUser.setId(1);
        logedUser.setPassword("password");

        given(userService.updateUser(any(), any(), any())).willThrow(new IllegalArgumentException("PUBLIC.USER(NICKNAME)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
        atr.put("logedUser", userController);

        controller.perform(post("/settings")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("views/updateUserForm"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewOther() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        given(userService.updateUser(any(), any(), any())).willThrow(new IllegalArgumentException("Other"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
        atr.put("logedUser", userController);
        // Act and Assert
        controller.perform(post("/settings")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("views/updateUserForm"));
    }  
    
    @WithMockUser(value = "spring")
    @Test
    public void testProcessUpdateplayerFormWhenFormIsValidThenRedirectToHome() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("valid@email.com");
        user.setId(2);

        Map<String, Object> atr = new HashMap<>();
            atr.put("user", user);
            atr.put("logedUser", userController);

        given(userService.updateUser(any(), any(), any())).willReturn(user);

        controller.perform(post("/settings")
                .flashAttrs(atr)
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testListUsersPaginationWhenValorIsValidThenReturnControlPanelView() throws Exception {
        // Arrange
        int valor = 0;
        User user1 = new User();
        user1.setId(1);
        user1.setNickname("user1");
        user1.setPassword("newPassword");
        user1.setEmail("user1@email.com");
        user1.setUserType(UserType.admin);
        User user2 = new User();
        user2.setId(2);
        user2.setNickname("user2");
        user2.setPassword("newPassword");
        user2.setEmail("user2@email.com");
        user2.setUserType(UserType.admin);
        User user3 = new User();
        user3.setId(1);
        user3.setNickname("user3");
        user3.setPassword("newPassword");
        user3.setEmail("user3@email.com");
        user3.setUserType(UserType.admin);
        User user4 = new User();
        user4.setId(1);
        user4.setNickname("user4");
        user4.setPassword("newPassword");
        user4.setEmail("user4@email.com");
        user4.setUserType(UserType.admin);
        List<User> users = Arrays.asList(
                user1,user2,user3,user4);
    
        Page<User> paginacion = new PageImpl<>(users);
    
        when(userService.findAllUser(any(), any())).thenReturn(paginacion);
    
        // Act and Assert
        controller.perform(get("/controlPanel")
                .param("valor", String.valueOf(valor))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/controlPanel"))
                .andExpect(model().attribute("paginas", 0))
                .andExpect(model().attribute("valores", 0))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("paginacion", paginacion));
    }


    @WithMockUser(value = "spring")
    @Test
    public void testListUsersPaginationWhenValorIsValidThenReturnControlPanelViewSecond() throws Exception {
        // Arrange
        int valor = 1;
        User user1 = new User();
        user1.setId(1);
        user1.setNickname("user1");
        user1.setPassword("newPassword");
        user1.setEmail("user1@email.com");
        user1.setUserType(UserType.admin);
        User user2 = new User();
        user2.setId(2);
        user2.setNickname("user2");
        user2.setPassword("newPassword");
        user2.setEmail("user2@email.com");
        user2.setUserType(UserType.admin);
        User user3 = new User();
        user3.setId(1);
        user3.setNickname("user3");
        user3.setPassword("newPassword");
        user3.setEmail("user3@email.com");
        user3.setUserType(UserType.admin);
        User user4 = new User();
        user4.setId(1);
        user4.setNickname("user4");
        user4.setPassword("newPassword");
        user4.setEmail("user4@email.com");
        user4.setUserType(UserType.admin);
        User user5 = new User();
        user5.setId(5);
        user5.setNickname("user5");
        user5.setPassword("newPassword");
        user5.setEmail("user5@email.com");
        user5.setUserType(UserType.admin);
        User user6 = new User();
        user6.setId(6);
        user6.setNickname("user6");
        user6.setPassword("newPassword");
        user6.setEmail("user6@email.com");
        user6.setUserType(UserType.admin);
        List<User> users = Arrays.asList(
                user1,user2,user3,user4,user5, user6);
    
        Page<User> paginacion = new PageImpl<>(users.subList(5, 6));
    
        when(userService.findAllUser(any(), any())).thenReturn(paginacion);
        when(userService.findAll()).thenReturn(users);

       

        controller.perform(get("/controlPanel")
                .param("valor", String.valueOf(valor))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/controlPanel"))
                .andExpect(model().attribute("paginas", 1))
                .andExpect(model().attribute("valores", 1))
                .andExpect(model().attribute("users", users.subList(5, 6)))
                .andExpect(model().attribute("paginacion", paginacion));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testDeleteUserWhenUserIsDeletedThenRedirectToControlPanel() throws Exception {
        // Arrange
        
        int id = 1;
        when(userService.deleteUser(id, userController)).thenReturn(true);

        // Act and Assert
        controller.perform(get("/controlPanel/delete/{idUserDeleted}", id)
                .flashAttr("logedUser", userController)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/controlPanel?valor=0"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testDeleteUserWhenUserIsDeletedThenNoRedirectToControlPanel() throws Exception {
        // Arrange
        
        int id = 1;
        when(userService.deleteUser(id, userController)).thenReturn(false);

        // Act and Assert
        controller.perform(get("/controlPanel/delete/{idUserDeleted}", id)
                .flashAttr("logedUser", userController)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testDeleteUserWhenUserIsDeletedThenNoRedirectToControlPanelException() throws Exception {
        // Arrange
        
        int id = 1;
        when(userService.deleteUser(id, userController)).thenThrow(new IllegalArgumentException(""));

        // Act and Assert
        controller.perform(get("/controlPanel/delete/{idUserDeleted}", id)
                .flashAttr("logedUser", userController)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testEnableUserSuccefull() throws Exception {
        // Arrange
        
        int id = 1;
        when(userService.enableUser(id, userController)).thenReturn(true);

        // Act and Assert
        controller.perform(get("/controlPanel/enable/{idUserEdited}", id)
                .flashAttr("logedUser", userController)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/controlPanel?valor=0"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testEnableUserFail() throws Exception {
        // Arrange
        
        int id = 1;
        when(userService.enableUser(id, userController)).thenReturn(false);

        // Act and Assert
        controller.perform(get("/controlPanel/enable/{idUserEdited}", id)
                .flashAttr("logedUser", userController)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));
    }


    @WithMockUser(value = "spring")
    @Test
    public void testAddUser() throws Exception {
        
        given(userService.findDistinctAuthorities()).willReturn(Arrays.asList(UserType.admin, UserType.player));
        controller.perform(get("/controlPanel/add"))
        .andExpect(status().isOk());

    }


    @WithMockUser(value = "spring")
    @Test
    public void testProcessAddplayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);


        given(userService.addUser(any(), any(), any(),any())).willThrow(new IllegalArgumentException("PUBLIC.USER(EMAIL)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
     

        controller.perform(post("/controlPanel/add")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/addUser"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessAddlayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewNickname() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        User logedUser = new User();
        logedUser.setId(1);
        logedUser.setPassword("password");

        given(userService.addUser(any(), any(), any(),any())).willThrow(new IllegalArgumentException("PUBLIC.USER(NICKNAME)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);

        controller.perform(post("/controlPanel/add")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/addUser"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessAddlayerFormWhenFormHasErrorsThenReturnUpdateUserFormViewOther() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        given(userService.addUser(any(), any(), any(), any())).willThrow(new IllegalArgumentException("Other"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
        
        controller.perform(post("/controlPanel/add")
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/addUser"));
    }  
    
    @WithMockUser(value = "spring")
    @Test
    public void testProcessAddPlayerFormWhenFormIsValid() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("valid@email.com");
        user.setId(2);

        Map<String, Object> atr = new HashMap<>();
            atr.put("user", user);

        given(userService.addUser(any(), any(), any(), any())).willReturn(user);

        controller.perform(post("/controlPanel/add")
                .flashAttrs(atr)
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/controlPanel/add"));
    }


    @WithMockUser(value = "spring")
    @Test
    public void testEditPlayerFormWhenFormIsInvalid() throws Exception {
        

        
        given(userService.findUserById(any())).willReturn(Optional.empty());

        controller.perform(get("/controlPanel/edit/{idUserEdited}", userController.getId())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/controlPanel/?valor=0"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testEditPlayerFormWhenFormIsValid() throws Exception {
        
        List<UserType> types = new ArrayList<>();
        types.add(UserType.admin);
        types.add(UserType.player);
        
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(userService.findDistinctAuthorities()).willReturn(types);

        controller.perform(get("/controlPanel/edit/{idUserEdited}", userController.getId())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/editUser"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessEditPlayerFormWhenFormIsValid() throws Exception {
        
        

        controller.perform(post("/controlPanel/edit/{idUserEdited}", userController.getId()).with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/controlPanel?valor=0"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testProcessEditPlayerFormWhenFormIsInvalidEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);


        given(userService.updateUser(any(), any(),any())).willThrow(new IllegalArgumentException("PUBLIC.USER(EMAIL)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
     

        controller.perform(post("/controlPanel/edit/{idUserEdited}", user.getId())
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/editUser"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessEditPlayerFormWhenFormIsInvalidNickname() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        User logedUser = new User();
        logedUser.setId(1);
        logedUser.setPassword("password");

        given(userService.updateUser(any(), any(),any())).willThrow(new IllegalArgumentException("PUBLIC.USER(NICKNAME)"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);

        controller.perform(post("/controlPanel/edit/{idUserEdited}", user.getId())
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/editUser"));
    }   

    @WithMockUser(value = "spring")
    @Test
    public void testProcessEditPlayerFormWhenFormIsInvalidOther() throws Exception {
        // Arrange
        User user = new User();
        user.setNickname("nickname-notValid");
        user.setPassword("password");
        user.setEmail("invalid-email");
        user.setId(2);

        given(userService.updateUser(any(), any(), any())).willThrow(new IllegalArgumentException("Other"));

        Map<String, Object> atr = new HashMap<>();
        atr.put("user", user);
        
        controller.perform(post("/controlPanel/edit/{idUserEdited}", user.getId())
                .with(csrf())
                .flashAttrs(atr))
               
                .andExpect(status().isOk())
                .andExpect(view().name("admin/editUser"));
    }  

    @WithMockUser(value = "spring")
    @Test
    public void detailsUserNoPresentTest() throws Exception {
        
        given(userService.findUserById(any())).willReturn(Optional.empty());

        controller.perform(get("/controlPanel/details/{idUserDetailed}", userController.getId()).with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name( "redirect:/controlPanel/?valor=0"));
    }


    @WithMockUser(value = "spring")
    @Test
    public void detailsUserTest() throws Exception {
        List<Object[]> listRes = new ArrayList<>();
        Object[] data = {achievement, Date.from(Instant.now())};
        listRes.add(data);
        given(userService.findUserById(any())).willReturn(Optional.of(userController));
        given(gameService.findTotalGamesPlayedByUser(any())).willReturn(0);
        given(gameService.findTotalTimePlayedByUser(any())).willReturn(0L);
        given(gameDetailsService.findPunctuationByNickname(any())).willReturn(0L);
        given(turnService.findTotalTurnsByUser(any())).willReturn(0);
        given(registerService.findRegistersByNickname(any())).willReturn(listRes);

        controller.perform(get("/controlPanel/details/{idUserDetailed}", userController.getId()).with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name( "admin/detailsUser"));
    }
    
}

