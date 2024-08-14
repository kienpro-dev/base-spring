package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.ImageService;
import com.example.projectbase.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CarMvc
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    private final AuthService authService;

    private final CarService carService;

    private final ImageService imageService;

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

    @GetMapping(UrlConstant.QUICK_VIEW)
	public ResponseEntity<Car> quickView(@PathVariable(name = "id") String id) {
        return new ResponseEntity<Car>(carService.findById(id).get(), HttpStatus.OK);
//		return VsResponseUtil.success(carService.findById(id).get());
	}

	@GetMapping(UrlConstant.QUICK_VIEW_IMAGES)
	public ResponseEntity<List<Image>> viewImageApi(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Image>>(imageService.findAllByCarId(id), HttpStatus.OK);
//		return VsResponseUtil.success(imageService.findAllByCarId(id));
	}



}
