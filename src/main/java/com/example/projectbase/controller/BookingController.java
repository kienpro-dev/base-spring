package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.BookingConstant;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.domain.dto.response.BookingDetailDto;
import com.example.projectbase.domain.dto.response.BookingDto;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.Wallet;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import com.example.projectbase.util.TimeOverlapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final WalletService walletService;

    @GetMapping("/check-out")
    public String checkOut(Model model, @RequestParam String id, @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }
        User user = userService.findById(userPrincipal.getId());
        model.addAttribute("user", user);
        CarDto item = carService.getCarById(id);
        model.addAttribute("item", item);
        model.addAttribute("startDate", sessionService.get("startDate"));
        model.addAttribute("startTime", sessionService.get("startTime"));
        model.addAttribute("endDate", sessionService.get("endDate"));
        model.addAttribute("endTime", sessionService.get("endTime"));
        sessionService.remove("startDate");
        sessionService.remove("startTime");
        sessionService.remove("endDate");
        sessionService.remove("endTime");
        return "client/cart/check-out";
    }

    @PostMapping("/check-out/submit")
    public String submitCheckOut(Model model, @ModelAttribute BookingDto bookingDto, @RequestParam(value = "id") String carId,
                                 @CurrentUser UserPrincipal user) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(user.getId());
            model.addAttribute("currentUser", currentUser);
        }
        Car item = carService.findById(carId).get();
        User userRent = userService.findById(user.getId());
        User owner = item.getUserOwn();
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
         bookingService.saveOrUpdate(booking);
        if (TimeOverlapUtil.checkTimeOverlap(start, end, bookingService.getBookingByCarId(carId))) {
            model.addAttribute("isFail", true);
            model.addAttribute("carId", carId);
        } else {
            if (userRent.getBalance() < booking.getTotal()) {
                model.addAttribute("failBalance", true);
                model.addAttribute("carId", carId);
            } else {
                userRent.setBalance(userRent.getBalance() - item.getDeposit());
                Wallet wallet = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(item.getName())
                        .fluctuation("-"+item.getDeposit())
                        .userOwn(userRent)
                        .type("Đặt cọc xe")
                        .build();
                walletService.saveOrUpdate(wallet);
                owner.setBalance(owner.getBalance() + item.getDeposit());
                Wallet walletOwner = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(item.getName())
                        .fluctuation("+"+item.getDeposit())
                        .userOwn(owner)
                        .type("Nhận cọc xe")
                        .build();
                walletService.saveOrUpdate(walletOwner);
                userService.saveOrUpdate(userRent);
                userService.saveOrUpdate(owner);

                model.addAttribute("isSuccess", true);
            }
        }

        model.addAttribute("isAuth", authService.isAuthenticated());
        model.addAttribute("user", userRent);
        model.addAttribute("item", item);
        return "client/cart/check-out";
    }

    @GetMapping("/booking-list")
    public String bookingList(Model model, @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
            List<Booking> bookings = bookingService.getBookingByUserId(userPrincipal.getId());
            model.addAttribute("list", bookings);
        } else {
            model.addAttribute("error", "Bạn chưa có đơn hàng nào.");
        }
        return "client/order/orderlist";
    }

    @GetMapping("/booking/confirm-pick-up")
    public String confirmPickUp(Model model, @CurrentUser UserPrincipal userPrincipal, @RequestParam String id) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }
        Booking booking = bookingService.getBookingById(id);
        booking.setStatus(BookingConstant.IN_PROGRESS);
        bookingService.saveOrUpdate(booking);
        return "redirect:/car/booking-list";
    }

    @GetMapping("/booking/cancel/{id}")
    public ResponseEntity<?> cancel(Model model, @CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "id") String id) {
        User currentUser = this.userService.findById(userPrincipal.getId());
        if (authService.isAuthenticated()) {
            model.addAttribute("currentUser", currentUser);
        }
        Booking booking = bookingService.getBookingById(id);
        booking.setStatus(BookingConstant.CANCEL);
        if (currentUser.getRole().getName().equals(RoleConstant.CAR_OWNER)) {
            User customer = booking.getUser();
            List<Car> carBooked = booking.getCars();
            for (Car car : carBooked) {
                customer.setBalance(customer.getBalance() + car.getDeposit());
                Wallet wallet = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(booking.getCars().get(0).getName())
                        .fluctuation("+"+car.getDeposit())
                        .userOwn(customer)
                        .type("Trả cọc xe")
                        .build();
                walletService.saveOrUpdate(wallet);
                 Wallet walletOwner = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(booking.getCars().get(0).getName())
                        .fluctuation("-"+car.getDeposit())
                        .userOwn(currentUser)
                        .type("Hoàn cọc xe")
                        .build();
                walletService.saveOrUpdate(walletOwner);
            }
            userService.saveOrUpdate(customer);
        }
        bookingService.saveOrUpdate(booking);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/booking/return-car")
    public String returnCar(Model model, @CurrentUser UserPrincipal userPrincipal, @RequestParam String id) {
        User currentUser = this.userService.findById(userPrincipal.getId());
        if (authService.isAuthenticated()) {
            model.addAttribute("currentUser", currentUser);
        }
        Booking booking = bookingService.getBookingById(id);
        List<Car> carBooked = booking.getCars();
        for (Car car : carBooked) {
            User owner = car.getUserOwn();
            if (booking.getTotal() <= car.getDeposit()) {
                currentUser.setBalance(currentUser.getBalance() + (car.getDeposit() - booking.getTotal()));
                 Wallet wallet = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(car.getName())
                        .fluctuation("+"+(car.getDeposit() - booking.getTotal()))
                        .userOwn(currentUser)
                        .type("Nhận tiền nhận xe")
                        .build();
                walletService.saveOrUpdate(wallet);
                owner.setBalance(owner.getBalance() - (car.getDeposit() - booking.getTotal()));
                Wallet walletOwner = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(car.getName())
                        .fluctuation("-"+(car.getDeposit() - booking.getTotal()))
                        .userOwn(owner)
                        .type("Trả tiền cọc cho khách")
                        .build();
                walletService.saveOrUpdate(walletOwner);
                booking.setStatus(BookingConstant.COMPLETED);
            } else {
                currentUser.setBalance(currentUser.getBalance() - (booking.getTotal() - car.getDeposit()));
                 Wallet wallet = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(car.getName())
                        .fluctuation("-"+(booking.getTotal() - car.getDeposit()))
                        .userOwn(currentUser)
                        .type("Thanh toán nốt tiền xe")
                        .build();
                walletService.saveOrUpdate(wallet);
                owner.setBalance(owner.getBalance() + (booking.getTotal() - car.getDeposit()));
                Wallet walletOwner = Wallet.builder()
                        .bookingNo(booking.getId())
                        .carName(car.getName())
                        .fluctuation("+"+(booking.getTotal() - car.getDeposit()))
                        .userOwn(owner)
                        .type("Nhận nốt tiền xe")
                        .build();
                walletService.saveOrUpdate(walletOwner);
                booking.setStatus(BookingConstant.PENDING_PAY);
            }
            userService.saveOrUpdate(owner);
            userService.saveOrUpdate(currentUser);
        }

        bookingService.saveOrUpdate(booking);
        return "redirect:/car/booking-list";
    }

    @GetMapping("/booking/confirm")
    public String confirm(Model model, @CurrentUser UserPrincipal userPrincipal, @RequestParam String id) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }
        Booking booking = bookingService.getBookingById(id);
        if (booking.getStatus().equals(BookingConstant.PENDING_DEPOSIT)) {
            booking.setStatus(BookingConstant.CONFIRM);

        } else if (booking.getStatus().equals(BookingConstant.PENDING_PAY)) {
            booking.setStatus(BookingConstant.COMPLETED);
        }
        bookingService.saveOrUpdate(booking);
        return "redirect:/car/account/info-account";
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<BookingDetailDto> viewByOrderId(@PathVariable(name = "id") String id) {
        BookingDetailDto bookingDetailDto = bookingService.getBookingDetail(id);
        return new ResponseEntity<BookingDetailDto>(bookingDetailDto, HttpStatus.OK);
    }

    @GetMapping(value = "/view-order-detail/{id}")
    public ResponseEntity<List<Car>> viewOrderDetailByOrderId(@PathVariable(name = "id") String id) {
        List<Car> list = carService.findCarByBookingId(id);
        return new ResponseEntity<List<Car>>(list, HttpStatus.OK);
    }
}
