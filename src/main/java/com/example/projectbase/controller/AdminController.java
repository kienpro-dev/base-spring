package com.example.projectbase.controller;

import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.LoginRequestDto;
import com.example.projectbase.domain.dto.request.UserRequestDto;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.CarRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.BookingService;
import com.example.projectbase.service.SessionService;
import com.example.projectbase.service.UserService;
import com.example.projectbase.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SessionService session;

    private final UserService userService;

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final BookingService bookingService;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Date datenow = new Date();

    @GetMapping(UrlConstant.Auth.ADMIN_LOGIN)
    public String getLoginForm(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "auth/admin/login";
    }

    @ModelAttribute("currentUser")
    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping(UrlConstant.Admin.ADMIN_HOME)
    public String getAdminPage(Model model) {
        model.addAttribute("updateDto", new UserRequestDto());
        return "admin/home/index";
    }

    @PostMapping(UrlConstant.Auth.ADMIN_LOGIN)
    public String loginSubmit(Model model, @Valid @ModelAttribute LoginRequestDto loginRequestDto, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        // authService.logoutMvc(request, response);
        if (loginRequestDto.getEmail().trim().isEmpty() || loginRequestDto.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin");
            return "auth/admin/login";
        }
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Thông tin tài khoản không chính xác");
            return "auth/admin/login";
        }
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            model.addAttribute("error", "Thông tin tài khoản không chính xác");
            return "auth/admin/login";
        }
        if (!user.getRole().getName().equals(RoleConstant.ADMIN)) {
            model.addAttribute("error", "Tài khoản không phải là tài khoản admin");
            return "auth/admin/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("error", "Lỗi định dạng");
            return "auth/admin/login";
        }
        authService.autoLogin(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return "redirect:/admin/home";
    }

    @GetMapping(value = UrlConstant.Admin.USERS_MANAGEMENT)
    public String list(Model model, @RequestParam(name = "field") Optional<String> field,
                       @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
                       @RequestParam(name = "keywords", defaultValue = "") Optional<String> keywords) {
        String keyword = keywords.orElse(session.get("keywords"));
        session.set("keywords", keyword);
        Sort sort = Sort.by(Sort.Direction.DESC, field.orElse("id"));
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
        Page<User> resultPage = userService.findAllByEmailLike("%" + keyword + "%", pageable);
        int totalPages = resultPage.getTotalPages();
        int startPage = Math.max(1, page.orElse(1) - 2);
        int endPage = Math.min(page.orElse(1) + 2, totalPages);
        if (totalPages > 5) {
            if (endPage == totalPages)
                startPage = endPage - 5;
            else if (startPage == 1)
                endPage = startPage + 5;
        }
        List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("field", field.orElse("id"));
        model.addAttribute("size", size.orElse(5));
        model.addAttribute("keywords", keyword);
        model.addAttribute("resultPage", resultPage);
        return "admin/users/user-list";
    }

    @PutMapping(value = UrlConstant.Admin.DELETE_USER)
    public ResponseEntity<Void> deleteApi(@PathVariable(name = "id") String id) throws IOException {
        userService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = UrlConstant.Admin.VIEW_USER)
    public ResponseEntity<?> viewApi(@PathVariable(name = "id") String id, Model model) {
        User user = userService.findById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping(value = UrlConstant.Admin.VIEW_ADMIN)
    public ResponseEntity<?> viewAdminApi(@PathVariable(name = "id") String id, Model model) {
        User user = userService.findById(id);
        return new ResponseEntity<UserRequestDto>(new UserRequestDto(user), HttpStatus.OK);
    }

    @GetMapping(value = UrlConstant.Admin.LOGOUT_ADMIN)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logoutMvc(request, response);
        return "redirect:/auth/admin/login";
    }

    @PutMapping(value = UrlConstant.Admin.UPDATE_INFOR)
    public ResponseEntity<Void> updateAPI(@ModelAttribute UserRequestDto updateDto, Model model) throws IOException {
        if (!userService.updateUser(updateDto)) {
            model.addAttribute("error", "Cập nhật không thành công!");
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = UrlConstant.Admin.CARS_MANAGEMENT)
    public String listCars(Model model, @RequestParam(name = "field") Optional<String> field,
                           @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
                           @RequestParam(name = "keywords", defaultValue = "") Optional<String> keywords) {
        String keyword = keywords.orElse(session.get("keywords"));
        session.set("keywords", keyword);
        Sort sort = Sort.by(Sort.Direction.DESC, field.orElse("id"));
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
        Page<Car> resultPage = carRepository.findAllByNameLike(pageable, keyword);
        int totalPages = resultPage.getTotalPages();
        int startPage = Math.max(1, page.orElse(1) - 2);
        int endPage = Math.min(page.orElse(1) + 2, totalPages);
        if (totalPages > 5) {
            if (endPage == totalPages)
                startPage = endPage - 5;
            else if (startPage == 1)
                endPage = startPage + 5;
        }
        List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("field", field.orElse("id"));
        model.addAttribute("size", size.orElse(5));
        model.addAttribute("keywords", keyword);
        model.addAttribute("resultPage", resultPage);
        return "admin/products/product-list";
    }

    @GetMapping(value = "/admin/count-order-date-now")
    public ResponseEntity<Integer> getCountOrderDateNow() {
        Date dateTo = DateUtil.sumNumberDate(datenow, 1);
        int countDateNow = bookingService.getBookingsByDate(formatter.format(datenow), formatter.format(dateTo));
        return new ResponseEntity<Integer>(countDateNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/count-order-date-ye")
    public ResponseEntity<Integer> getCountOrderDateYe() {
        Date dateYe = DateUtil.sumNumberDate(datenow, -1);
        int countDateYe = bookingService.getBookingsByDate(formatter.format(dateYe), formatter.format(datenow));
        return new ResponseEntity<Integer>(countDateYe, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/count-order-month-now")
    public ResponseEntity<Integer> getCountOrderMonthNow() {
        int countMonthNow = bookingService.getBookingsByMonth(DateUtil.getMonthNow() + 1);
        return new ResponseEntity<Integer>(countMonthNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/count-order-month-ye")
    public ResponseEntity<Integer> getCountOrderMonthYe() {
        int countMonthYe = bookingService.getBookingsByMonth(DateUtil.getMonthNow());
        return new ResponseEntity<Integer>(countMonthYe, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/count-order-year-now")
    public ResponseEntity<Integer> getCountOrderYearNow() {
        int countYearNow = bookingService.getBookingsByYear(DateUtil.getYearNow());
        return new ResponseEntity<Integer>(countYearNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/count-order-year-ye")
    public ResponseEntity<Integer> getCountOrderYearYe() {
        int countYearYe = bookingService.getBookingsByYear(DateUtil.getYearNow() - 1);
        return new ResponseEntity<Integer>(countYearYe, HttpStatus.OK);
    }


    @GetMapping(value = "/admin/users-date-now")
    public ResponseEntity<Integer> getCustomersDateNow() {
        Date dateTo = DateUtil.sumNumberDate(datenow, 1);
        int customersDateNow = userService.getCustomersByDate(formatter.format(datenow), formatter.format(dateTo));
        return new ResponseEntity<Integer>(customersDateNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users-date-ye")
    public ResponseEntity<Integer> getCustomersDateYe() {
        Date dateYe = DateUtil.sumNumberDate(datenow, -1);
        int customersDateYe = userService.getCustomersByDate(formatter.format(dateYe), formatter.format(datenow));
        return new ResponseEntity<Integer>(customersDateYe, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users-month-now")
    public ResponseEntity<Integer> getCustomersMonthNow() {
        int customersMonthNow = userService.getCustomersByMonth(DateUtil.getMonthNow() + 1);
        return new ResponseEntity<Integer>(customersMonthNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users-month-ye")
    public ResponseEntity<Integer> getCustomersMonthYe() {
        int customersMonthYe = userService.getCustomersByMonth(DateUtil.getMonthNow());
        return new ResponseEntity<Integer>(customersMonthYe, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users-year-now")
    public ResponseEntity<Integer> getCustomersYearNow() {
        int customersYearNow = userService.getCustomersByYear(DateUtil.getYearNow());
        return new ResponseEntity<Integer>(customersYearNow, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users-year-ye")
    public ResponseEntity<Integer> getCustomersYearYe() {
        int customersYearYe = userService.getCustomersByYear(DateUtil.getYearNow() - 1);
        return new ResponseEntity<Integer>(customersYearYe, HttpStatus.OK);
    }





}
