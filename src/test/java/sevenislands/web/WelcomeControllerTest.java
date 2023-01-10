/*package sevenislands.web;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import sevenislands.configuration.SecurityConfiguration;
import sevenislands.user.User;
import sevenislands.user.UserService;

@WebMvcTest(value = WelcomeController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class WelcomeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

  

    @Test
    public void testWelcome_whenAuthenticationIsNull_thenReturnWelcomeView() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    public void testWelcome_whenAuthenticationIsAnonymous_thenReturnWelcomeView() throws Exception {
        // Arrange
        when(SecurityContextHolder.getContext().getAuthentication())
                .thenReturn(new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));

        // Act and Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    public void testWelcome_whenLogedUserIsNull_thenReturnWelcomeView() throws Exception {
        // Arrange
        when(SecurityContextHolder.getContext().getAuthentication())
                .thenReturn(new TestingAuthenticationToken("user", "password", "ROLE_USER"));

        // Act and Assert
        mockMvc.perform(get("/")
                .sessionAttr("logedUser", null)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    public void testWelcome_whenUserIsNotFound_thenReturnWelcomeView() throws Exception {
        // Arrange
        when(SecurityContextHolder.getContext().getAuthentication())
                .thenReturn(new TestingAuthenticationToken("user", "password", "ROLE_USER"));
        User logedUser = new User();
        logedUser.setId(1);
        when(userService.findUserById(logedUser.getId())).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/")
                .sessionAttr("logedUser", logedUser)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    public void testWelcome_whenUserIsFound_thenReturnHomeView() throws Exception {
        // Arrange
       // when(SecurityContextHolder.getContext().getAuthentication())
        //        .thenReturn(new TestingAuthenticationToken("user", "password", "ROLE_USER"));
        given(SecurityContextHolder.getContext().getAuthentication()).willReturn(new TestingAuthenticationToken("user", "password", "ROLE_USER"));
        User logedUser = new User();
        logedUser.setId(1);
        given(userService.findUserById(logedUser.getId())).willReturn(Optional.of(logedUser));
        //when(userService.findUserById(logedUser.getId())).thenReturn(Optional.of(logedUser));

        // Act and Assert
        mockMvc.perform(get("/").flashAttr("logedUser", logedUser))
        .andExpect(status().is3xxRedirection());


    }
}*/
