package com.example.projectbase.service;

public interface BookingService {
    int getBookingsByDate(String dateNow, String dateTo);

    int getBookingsByMonth(int month);

    int getBookingsByYear(int year);
}
