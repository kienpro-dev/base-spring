package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.LoginRequestDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

  String email();

  boolean isAuthenticated();

  void autoLogin(String email, String password);

  void logoutMvc(HttpServletRequest request, HttpServletResponse response);

  boolean checkCurrentUserPassword(String password);

  boolean checkEmailMatchPassword(LoginRequestDto login);

  boolean checkEmailRegistered(String email);
}
