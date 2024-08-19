package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.Feedback;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CarMvc
@RequiredArgsConstructor
public class ReportController {
    private final UserService userService;

    private final AuthService authService;

    private final CarService carService;

    private final BookingService bookingService;

    private final FeedbackService feedbackService;

    @GetMapping("/report")
    public String viewReport(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size,
                             @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
            Sort sort = Sort.by(Sort.Direction.DESC, "created_date");
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Feedback> feedbackPage = this.feedbackService.findAllFeedBackByUserId(currentUser.getId(), pageable);
            List<Feedback> feedbackList = feedbackPage.getContent();

            double totalRate = 0;
            int count = 0;
            for (Feedback feedback : feedbackList) {
                if (feedback.getRating() != null) {
                    totalRate += feedback.getRating();
                    count++;
                }
            }
            double avgRate = totalRate != 0 ? totalRate / count : 0;

            int totalPages = feedbackPage.getTotalPages();
            int startPage = Math.max(1, page + 1 - 2);
            int endPage = Math.min(page + 1 + 2, totalPages);
            if (totalPages > 5) {
                if (endPage == totalPages)
                    startPage = endPage - 5;
                else if (startPage == 1)
                    endPage = startPage + 5;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("resultPage", feedbackPage);
            model.addAttribute("feedbackList", feedbackList);
            model.addAttribute("countAllFeedback", feedbackList.size());
            model.addAttribute("countFeedbackByRating1", this.feedbackService.countFeedbackByRating(1));
            model.addAttribute("countFeedbackByRating2", this.feedbackService.countFeedbackByRating(2));
            model.addAttribute("countFeedbackByRating3", this.feedbackService.countFeedbackByRating(3));
            model.addAttribute("countFeedbackByRating4", this.feedbackService.countFeedbackByRating(4));
            model.addAttribute("countFeedbackByRating5", this.feedbackService.countFeedbackByRating(5));
            model.addAttribute("avgRate", avgRate);
        }
        return "/client/carOwner/viewReport";
    }
}
