package com.example.projectbase.controller;


import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.RegisterRequestDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@Controller
//@RestApiV1
public class AuthController {

  private final AuthService authService;

  private final UserRepository userRepository;

  private final UserService userService;

  @GetMapping(UrlConstant.Auth.LOGIN)
  public String getLoginPage(){
    return "login";
  }

  @GetMapping("/")
  @ResponseBody
  public String getHomePage(){
    return "Welcome";
  }

  @GetMapping("/admin/home")
  public String getAdminPage(Model model){
    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
      UserPrincipal user= (UserPrincipal) authentication.getPrincipal();
      model.addAttribute("currentUser",user.getName());
    }

    return "/admin/index";
  }

  @GetMapping(UrlConstant.Auth.REGISTER)
  public String getRegisterPage(Model model){
    model.addAttribute("requestDto",new RegisterRequestDto());
    return "register";
  }


  @PostMapping(UrlConstant.Auth.REGISTER)
  public String register(@ModelAttribute("requestDto")  RegisterRequestDto requestDto, Model model){
    if(userService.createUser(requestDto)){
      model.addAttribute("succes","Đăng ký tài khoản thành công!");
    }
    else if(userRepository.existsByEmail(requestDto.getEmail())){
      model.addAttribute("message","Email đã tồn tại!");
    }
    else if(!requestDto.getPassword().equals(requestDto.getRepeatPassword())){
      model.addAttribute("message","Mật khẩu không khớp!");
    }
    return "register";
  }
}
