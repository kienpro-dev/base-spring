package com.example.projectbase.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String paymentMethod;

}
