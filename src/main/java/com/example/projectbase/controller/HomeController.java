package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.constant.UrlConstant;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@CarMvc
public class HomeController {
    @GetMapping(UrlConstant.HOME)
    public String getPage(Model model) {
        return "client/home/index";
    }
}
