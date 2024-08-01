package com.example.projectbase.controller;

import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.UserUpdateRequestDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.SessionService;
import com.example.projectbase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SessionService session;

    private final UserService userService;

    @GetMapping(UrlConstant.Admin.ADMIN_LOGIN)
    public String getLoginForm(){
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
        return "admin/home/index";
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


    @DeleteMapping(value = UrlConstant.Admin.DELETE_USER)
    public ResponseEntity<Void> deleteApi(@PathVariable(name = "id") String id) throws IOException {
        userService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = UrlConstant.Admin.VIEW_USER)
    public ResponseEntity<User> viewApi(@PathVariable(name = "id") String id) {
        User user = userService.findById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


}
