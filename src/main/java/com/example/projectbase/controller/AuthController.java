package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.LoginRequestDto;
import com.example.projectbase.domain.dto.request.RegisterRequestDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.UserService;
import com.example.projectbase.validator.annotation.ValidFileImage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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


  //Test Login Succes
  @GetMapping("/")
  @ResponseBody
  public String getHomePage(){
    return "Welcome";
  }

  @GetMapping("/admin/home")
  @ResponseBody
  public String getAdminPage(){
    return "Hello Admin";
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
