package com.example.projectbase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/error")
public class ErrorController  {

    @GetMapping(value = "/403")
    public String page403() {
        return "auth/error/admin/403";
    }

    @GetMapping(value = "/404")
    public String page404() {

        return "auth/error/admin/404";
    }
}
