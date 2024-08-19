package com.example.projectbase.service.impl;

import com.example.projectbase.domain.entity.Feedback;
import com.example.projectbase.repository.FeedbackRepository;
import com.example.projectbase.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Override
    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}
