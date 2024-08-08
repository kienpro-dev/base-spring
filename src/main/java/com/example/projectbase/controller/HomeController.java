package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CarMvc
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    private final AuthService authService;

    private final CarService carService;

    @GetMapping(UrlConstant.HOME)
    public String home(Model model,
                       @CurrentUser UserPrincipal userPrincipal) {
        if(authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("user", currentUser);
            model.addAttribute("role", currentUser.getRole().getName());
        }

        List<Car> listCarPetrol = carService.findAllByFuelType("Petrol");
        model.addAttribute("listPetrol", listCarPetrol);

        List<Car> listCarElectric = carService.findAllByFuelType("Electric");
        model.addAttribute("listElectric", listCarElectric);

        return "client/home/index";
    }

    @GetMapping(UrlConstant.ABOUT)
    public String about() {
        return "client/about/about";
    }

    @GetMapping(UrlConstant.CONTACT)
    public String contact() {
        return "client/contact/contact";
    }
}
