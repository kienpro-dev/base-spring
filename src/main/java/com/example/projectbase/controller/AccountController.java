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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@CarMvc
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    private final AuthService authService;

    @GetMapping("/account/info-account")
    public String infoAccount(Model model) {
		OrderAddressDto orderAddressDto = new OrderAddressDto();
        if(authService.isAuthenticated()) {
            String email = authService.email();
            User user = userService.findByEmail(email);
            UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setName(user.getName());
			userDto.setEmail(user.getEmail());
			userDto.setAddress(user.getAddress());
			userDto.setRoleName(user.getRole().getName());
			userDto.setAddress(user.getAddress());
			userDto.setDrivingLicense(user.getDrivingLicense());
			userDto.setNationalId(user.getNationalId());
			userDto.setPhoneNumber(user.getPhoneNumber());
			userDto.setActive(user.getIsActive());
			model.addAttribute("userDto", userDto);

			orderAddressDto.setId(userDto.getId());
			orderAddressDto.setPhoneNumber(user.getPhoneNumber());
			orderAddressDto.setAddress(userDto.getAddress());
			orderAddressDto.setName(userDto.getName());
			orderAddressDto.setEmail(userDto.getEmail());
			model.addAttribute("orderAddressDto", orderAddressDto);
        }

        return "client/account/info-account";
    }

    @PostMapping(value = "/account/info-account/change-info")
	public String changeInfoSubmit(Model model,
                                   @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Lỗi định dạng");
			return "client/account/info-account";
		}
		User user = userService.findById(userDto.getId());
		user.setName(userDto.getName());
		user.setAddress(userDto.getAddress());
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setNationalId(userDto.getNationalId());
		user.setDrivingLicense(userDto.getDrivingLicense());
		userService.saveOrUpdate(user);
		model.addAttribute("message", "Đã cập nhật thông tin tài khoản thành công.");
		return "redirect:/car/account/info-account";
	}

	@PostMapping(value = "/account/info-account/order-address")
	public String changeOrderAddressSubmit(Model model,
								   @Valid @ModelAttribute("orderAddressDto") OrderAddressDto orderAddressDto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", "Lỗi định dạng");
			return "client/account/info-account";
		}

		User user=userService.findById(orderAddressDto.getId());
		user.setAddress(orderAddressDto.getAddress());
		user.setName(orderAddressDto.getName());
		user.setPhoneNumber(orderAddressDto.getPhoneNumber());
		user.setEmail(orderAddressDto.getEmail());
		userService.saveOrUpdate(user);

		model.addAttribute("message", "Đã cập nhật thông tin  thành công.");
		return "redirect:/car/account/info-account";
	}
}
