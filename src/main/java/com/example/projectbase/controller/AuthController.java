package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.ChangePasswordRequestDto;
import com.example.projectbase.domain.dto.request.LoginRequestDto;
import com.example.projectbase.domain.dto.request.MailRequestDto;
import com.example.projectbase.domain.dto.request.RegisterRequestDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.SessionService;
import com.example.projectbase.service.UserService;
import com.example.projectbase.util.CryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@CarMvc
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final SessionService sessionService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @PostMapping(value = UrlConstant.Auth.LOGIN)
    @ResponseBody
    public ResponseEntity<?> loginSubmit(Model model,@RequestBody LoginRequestDto loginRequestDto) {

        if (!userService.existsByEmail(loginRequestDto.getEmail()) || !authService.checkEmailMatchPassword(loginRequestDto))
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Thông tin tài khoản không chính xác");
        authService.autoLogin(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return VsResponseUtil.success("Chúc mừng bạn đã đăng nhập thành công.");
    }


    @PostMapping(value = UrlConstant.Auth.REGISTER)
    @ResponseBody
    public ResponseEntity<?> registerSubmit(@RequestBody RegisterRequestDto register) {
        if (userService.existsByPhoneNumber(register.getPhoneNumber()))
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Số điện thoại đã được sử dụng");
        if (userService.existsByEmail(register.getEmail()))
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Email đã được sử dụng");
        User user = new User();
        user.setName(register.getName());
        user.setPhoneNumber(register.getPhoneNumber());
        user.setEmail(register.getEmail());
        user.setPassword(register.getPassword());
        user.setRole(roleRepository.findByRoleName(RoleConstant.USER));
        user.setIsActive(true);
        userService.saveOrUpdate(user);
        return VsResponseUtil.success("Chúc mừng bạn đã đăng ký thành công.");
    }

    @GetMapping(value = UrlConstant.Auth.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logoutMvc(request, response);
        return VsResponseUtil.success("Đăng xuất thành công.");
    }

    @PostMapping(UrlConstant.Auth.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto changePassword) {
        if (!authService.isAuthenticated()) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Yêu cầu đăng nhập để đổi mật khẩu");
        }
        if (!authService.checkCurrentUserPassword(changePassword.getPassword())) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Mật khẩu người dùng không đúng");
        }
        userService.changePassword(authService.email(), changePassword.getNewPassword());
        return VsResponseUtil.success("Chúc mừng bạn đã đổi mật khẩu thành công.");
    }

    @PostMapping(UrlConstant.Auth.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody MailRequestDto mailRequest) throws Exception {
        if (!authService.checkEmailRegistered(mailRequest.getEmail())) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Email chưa được đăng ký");
        }
        userService.sendMail(mailRequest.getEmail(), mailRequest.getUrl());
        return VsResponseUtil.success(HttpStatus.OK, "Chúc mừng bạn đã gửi yêu cầu thành công.");
    }

    @GetMapping(value = UrlConstant.Auth.RESET_PASSWORD)
    public String resetPassword(Model model, @RequestParam(name = "email") String email) {
        sessionService.set("reset-password", email);
        return "auth/admin/reset-password";
    }

    @PostMapping(value = UrlConstant.Auth.SUBMIT_RESET)
    public String resetPasswordSubmit(Model model, @RequestParam(name = "password") String password,
                                      @RequestParam(name = "againPassword") String againPassword,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!password.equals(againPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp với mật khẩu. Vui lòng nhập lại");
            return "auth/admin/reset-password";
        }
        String email = sessionService.get("reset-password");
        String decryptedEmail = CryptionUtil.decrypt(email.trim(), "RentalCar");
        Optional<User> user = userService.changePassword(decryptedEmail, password);
        sessionService.remove("reset-password");
        return "redirect:/car/home";
    }
}
