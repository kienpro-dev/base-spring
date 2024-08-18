package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.response.OrderAddressDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.Wallet;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.BookingService;
import com.example.projectbase.service.UserService;
import com.example.projectbase.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CarMvc
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    private final AuthService authService;

    private final BookingService bookingService;

    private final WalletService walletService;

    @GetMapping("/account/info-account")
    public String infoAccount(Model model, @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
            String email = authService.email();
            User user = userService.findByEmail(email);
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setAddress(user.getAddress());
            if (user.getDateOfBirth() != null) {
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
            List<Wallet> listWallet=walletService.getWalletByUserId(user.getId());
            model.addAttribute("listWallet",listWallet);
            if (user.getRole().getName().equals(RoleConstant.CAR_OWNER)) {
                List<Booking> bookings = bookingService.getBookingByCarOwnerId(userPrincipal.getId());
                model.addAttribute("list", bookings);
            } else {
                List<Booking> bookings = bookingService.getBookingByUserId(userPrincipal.getId());
                model.addAttribute("list", bookings);
            }

        }
        return "client/account/info-account";
    }

    @PostMapping(value = "/account/info-account/change-info")
    public String changeInfoSubmit(Model model,
                                   @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result,
                                   @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }

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

    @PostMapping(value = "/account/info-account/{id}/balance", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> balance(
            @PathVariable String id,
            @RequestParam Double balance,
            @RequestParam String type) {
        User user = userService.findById(id);
        Map<String, String> response = new HashMap<>();
        String message;
        if (type.equals("add")) {
            user.setBalance(user.getBalance() + balance);
            userService.saveOrUpdate(user);
            message = "Nạp tiền thành công.";
            Wallet wallet= Wallet.builder()
                    .bookingNo("N/A")
                    .carName("N/A")
                    .fluctuation("+"+balance.toString())
                    .userOwn(user)
                    .type("Nạp tiền")
                    .build();
            walletService.saveOrUpdate(wallet);
        } else if (type.equals("minus")) {
            if (balance > user.getBalance()) {
                message = "Số tiền còn lại không đủ để rút.";
                response.put("error", message);
                return ResponseEntity.badRequest().body(response);
            } else {
                user.setBalance(user.getBalance() - balance);
                userService.saveOrUpdate(user);
                message = "Rút tiền thành công.";
                  Wallet wallet= Wallet.builder()
                    .bookingNo("N/A")
                    .carName("N/A")
                    .fluctuation("-"+balance.toString())
                    .userOwn(user)
                    .type("Rút tiền")
                    .build();
            walletService.saveOrUpdate(wallet);
            }
        } else {
            message = "Hành động không hợp lệ.";
            response.put("error", message);
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", message);
        return ResponseEntity.ok(response);
    }

}
