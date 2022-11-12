package org.springframework.samples.sevenislands.tools;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ehcache.shadow.org.terracotta.context.ContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.sevenislands.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class methods {

    private static UserService userService;

    @Autowired
	public methods(UserService userService) {
		this.userService = userService;
	}

    public static void checkUser(HttpServletRequest request) throws ServletException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userService.checkUserByName(principal.getUsername())) {
            request.getSession().invalidate();
            request.logout();
        }
    }
}
