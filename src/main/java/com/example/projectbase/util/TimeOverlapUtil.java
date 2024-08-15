package com.example.projectbase.util;

import com.example.projectbase.domain.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public class TimeOverlapUtil {
    public static boolean checkTimeOverlap(LocalDateTime start, LocalDateTime end, List<Booking> bookingList) {
        for (Booking booking : bookingList) {
            if (start.isBefore(booking.getEndDate()) && end.isAfter(booking.getStartDate())) {
                return true;
            }
        }
        return false;
    }
}
