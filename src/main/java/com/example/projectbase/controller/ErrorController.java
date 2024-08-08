package com.example.projectbase.controller;

import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/error")
public class ErrorController  {

    @ModelAttribute("currentUser")
    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping(value = "/403")
    public String page403() {
        return "auth/error/admin/403";
    }

    @GetMapping(value = "/404")
    public String page404() {
        return "auth/error/admin/404";
    }
}
