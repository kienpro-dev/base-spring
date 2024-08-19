package com.example.projectbase.service;

import com.example.projectbase.domain.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {
    Feedback save(Feedback feedback);
    Page<Feedback> findAllFeedBackByUserId(String UserId, Pageable pageable);
    Integer countFeedbackByRating(Integer rating, String userOwnId);
}
