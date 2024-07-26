package com.example.projectbase.service.impl;

import com.example.projectbase.domain.dto.request.LoginRequestDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public String email() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
		return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public void autoLogin(String email, String password) {
        User user = userService.findByEmail(email).get();
        userService.updateLastLoginDate(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", email));
        }
    }

    @Override
    public void logoutMvc(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
    }

    @Override
    public boolean checkCurrentUserPassword(String password) {
        Optional<User> user = userService.findByEmail(email());
		if(passwordEncoder.matches(password, user.get().getPassword())) {
			return true;
		}
		return false;
    }

    @Override
    public boolean checkEmailMatchPassword(LoginRequestDto login) {
        Optional<User> user = userService.findByEmail(login.getEmail());
		if(passwordEncoder.matches(login.getPassword(), user.get().getPassword())) {
			return true;
		}
		return false;
    }

    @Override
    public boolean checkEmailRegistered(String email) {
        Optional<User> user = userService.findByEmail(email);
		if(user.isPresent()) {
			return true;
		}
		return false;
    }

}
