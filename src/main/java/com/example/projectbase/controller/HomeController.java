package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.UrlConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CarMvc
public class HomeController {
    @GetMapping(UrlConstant.HOME)
    public String home(Model model) {
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
