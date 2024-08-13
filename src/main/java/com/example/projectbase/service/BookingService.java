package com.example.projectbase.service;

import com.example.projectbase.domain.entity.Booking;

public interface BookingService {
    Booking saveOrUpdate(Booking booking);

//    void saveDetail(String bookingId, String carId);
}
