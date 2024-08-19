package com.example.projectbase.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {
    private Double rating;
    private String content;
    private String bookingId;
}