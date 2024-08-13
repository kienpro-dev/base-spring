package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.response.OrderAddressDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@CarMvc
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    private final AuthService authService;

    @GetMapping("/account/info-account")
    public String infoAccount(Model model) {
        if(authService.isAuthenticated()) {
            String email = authService.email();
            User user = userService.findByEmail(email);
            UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setName(user.getName());
			userDto.setEmail(user.getEmail());
			userDto.setAddress(user.getAddress());
			if(user.getDateOfBirth() != null) {
				userDto.setDateOfBirth(Date.from(user.getDateOfBirth().atZone(ZoneId.systemDefault()).toInstant()));
			}
			userDto.setRoleName(user.getRole().getName());
			userDto.setAddress(user.getAddress());
			userDto.setDrivingLicense(user.getDrivingLicense());
			userDto.setNationalId(user.getNationalId());
			userDto.setPhoneNumber(user.getPhoneNumber());
			userDto.setBalance(user.getBalance());
			userDto.setActive(user.getIsActive());
			model.addAttribute("userDto", userDto);
        }
        return "client/account/info-account";
    }

    @PostMapping(value = "/account/info-account/change-info")
	public String changeInfoSubmit(Model model,
                                   @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
//		if (result.hasErrors()) {
//			model.addAttribute("error", "Lỗi định dạng");
//			return "client/account/info-account";
//		}
		User user = userService.findById(userDto.getId());
		user.setName(userDto.getName());
		user.setAddress(userDto.getAddress());
		user.setDateOfBirth(LocalDateTime.ofInstant(userDto.getDateOfBirth().toInstant(), ZoneId.systemDefault()));
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setNationalId(userDto.getNationalId());
		user.setDrivingLicense(userDto.getDrivingLicense());
		userService.saveOrUpdate(user);
		model.addAttribute("message", "Đã cập nhật thông tin tài khoản thành công.");
		return "redirect:/car/account/info-account";
	}

	 @PostMapping(value = "/account/info-account/{id}/balance")
	public String balance(Model model, @PathVariable String id, @RequestParam Double balance) {
//		if (result.hasErrors()) {
//			model.addAttribute("error", "Lỗi định dạng");
//			return "client/account/info-account";
//		}
		User user = userService.findById(id);
		user.setBalance(user.getBalance() + balance);
		userService.saveOrUpdate(user);
		model.addAttribute("message", "Đã cập nhật số dư tài khoản thành công.");
		return "redirect:/car/account/info-account";
	}
}
