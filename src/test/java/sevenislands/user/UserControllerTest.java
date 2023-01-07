package sevenislands.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import sevenislands.configuration.SecurityConfiguration;
/* 
@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
	private MockMvc controller;

    @Autowired
    private UserService userService;

    @WithMockUser(value = "spring")
    @Test
    public void testInitUpdateOwnerForm() throws Exception {
        // Arrange
        User logedUser = new User();
        logedUser.setNickname("user1");
        logedUser.setPassword("password");
        given(userService.checkUser(any(), logedUser)).willReturn(false);

        // Act
        controller.perform(get("/settings", logedUser)).andExpect(status().isOk());

    }

@Test
public void testProcessUpdateplayerForm_validUser_loginSuccessful() {
    // Arrange
    User logedUser = new User();
    logedUser.setNickname("user1");
    logedUser.setPassword("password");
    User user = new User();
    user.setNickname("user1");
    user.setPassword("newPassword");
    user.setEmail("user1@email.com");
    HttpServletRequest request = mock(HttpServletRequest.class);
    ModelMap model = new ModelMap();
    when(request.getSession()).thenReturn(mock(HttpSession.class));
    when(request.getSession().getAttribute("logedUser")).thenReturn(logedUser);
    BindingResult result = mock(BindingResult.class);
    when(result.hasErrors()).thenReturn(false);

    // Act

  
  
}
}
*/
