package com.example.projectbase.service;


import com.example.projectbase.domain.entity.Booking;

import java.util.List;

public interface BookingService {
    int getBookingsByDate(String dateNow, String dateTo);

    int getBookingsByMonth(int month);

    int getBookingsByYear(int year);

    Booking saveOrUpdate(Booking booking);

    List<Booking> getBookingByCarId(String carId);

    List<Booking> getBookingByUserId(String userId);

//    void saveDetail(String bookingId, String carId);

}