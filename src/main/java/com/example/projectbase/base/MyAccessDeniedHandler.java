package com.example.projectbase.base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.projectbase.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
    AuthService authService;

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AccessDeniedException e) throws IOException, ServletException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// CustomUserDetailsService userDetails = (CustomUserDetailsService)
		// auth.getPrincipal();
		// System.out.println(userDetails.getPassword() + userDetails.getUsername()+
		// userDetails.getAuthorities());
		authService.logoutMvc(httpServletRequest, httpServletResponse);
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/error/403");
	}
}
