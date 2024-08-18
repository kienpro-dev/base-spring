package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CarMvc
@RequiredArgsConstructor
public class CarDetailController {
    private final CarService carService;

    private final AuthService authService;

    private final UserService userService;

    private final ImageService imageService;

    private final SessionService sessionService;

    @GetMapping("/product-detail")
    public String viewDetails(Model model, @RequestParam(name = "id") String id, @CurrentUser UserPrincipal userPrincipal) {
        if(authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }
        CarDto carDto = this.carService.getCarById(id);
        List<Booking> bookings = carDto.getBookings();
        Booking lastBooking = null;
        if (!bookings.isEmpty()) {
            lastBooking = bookings.get(bookings.size() - 1);
        }
        model.addAttribute("lastBooking", lastBooking);
        model.addAttribute("carDto", carDto);
        model.addAttribute("id", id);
        model.addAttribute("startDate", sessionService.get("startDate"));
        model.addAttribute("startTime", sessionService.get("startTime"));
        model.addAttribute("endDate", sessionService.get("endDate"));
        model.addAttribute("endTime", sessionService.get("endTime"));
        return "client/productDetail/details";
    }
}
