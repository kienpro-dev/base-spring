package com.example.projectbase.service;


import com.example.projectbase.domain.entity.Booking;

public interface BookingService {
    int getBookingsByDate(String dateNow, String dateTo);

    int getBookingsByMonth(int month);

    int getBookingsByYear(int year);

    Booking saveOrUpdate(Booking booking);

//    void saveDetail(String bookingId, String carId);

}