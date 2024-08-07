package com.example.projectbase.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().toString();
        if (role.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/home");
        } else if (role.contains("ROLE_USER")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("auth/login");
        }
    }
}