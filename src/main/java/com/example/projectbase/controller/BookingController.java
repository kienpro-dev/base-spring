package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.BookingConstant;
import com.example.projectbase.domain.dto.response.BookingDto;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import com.example.projectbase.util.TimeOverlapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@CarMvc
@RequiredArgsConstructor
public class BookingController {
    private final AuthService authService;

    private final SessionService sessionService;

    private final UserService userService;

    private final CarService carService;

    private final BookingService bookingService;

    @GetMapping("/check-out")
    public String checkOut(Model model, @RequestParam String id, @CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        model.addAttribute("user", user);
        CarDto item = carService.getCarById(id);
        model.addAttribute("item", item);
        return "client/cart/check-out";
    }

    @PostMapping("/check-out/submit")
    public String submitCheckOut(Model model, @ModelAttribute BookingDto bookingDto,
                                @CurrentUser UserPrincipal user, @RequestParam(value = "id") String carId) {
        Car item = carService.findById(carId).get();
        User userRent = userService.findById(user.getId());
        Booking booking = new Booking();
        booking.setBookingNo(new Random().nextInt(100000 - 1000 + 1));
        LocalDateTime start = LocalDate.parse(bookingDto.getStartDate()).atTime(LocalTime.parse(bookingDto.getStartTime()));
        LocalDateTime end = LocalDate.parse(bookingDto.getEndDate()).atTime(LocalTime.parse(bookingDto.getEndTime()));
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setDriverInfo(userRent.getName());
        booking.setPaymentMethod(bookingDto.getPaymentMethod());
        booking.setStatus(BookingConstant.PENDING_DEPOSIT);
        booking.setUser(userRent);
        List<Car> cars = new ArrayList<>();
        cars.add(item);
        booking.setCars(cars);
        if(TimeOverlapUtil.checkTimeOverlap(start, end, bookingService.getBookingByCarId(carId))) {
            model.addAttribute("isFail", true);
            model.addAttribute("carId", carId);
        } else {
            bookingService.saveOrUpdate(booking);
            model.addAttribute("isSuccess", true);
        }

        model.addAttribute("isAuth", authService.isAuthenticated());
        model.addAttribute("user", userRent);
        model.addAttribute("item", item);
        return "client/cart/check-out";
    }

    @GetMapping("/booking-list")
    public String bookingList(Model model, @CurrentUser UserPrincipal userPrincipal) {
        if(authService.isAuthenticated()) {
            List<Booking> bookings = bookingService.getBookingByUserId(userPrincipal.getId());
            model.addAttribute("list", bookings);
        } else {
			model.addAttribute("error", "Bạn chưa có đơn hàng nào.");
		}
        return "client/order/orderlist";
    }
}
