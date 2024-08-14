package com.example.projectbase.service.impl;


import com.example.projectbase.domain.entity.Booking;

import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public int getBookingsByDate(String dateNow, String dateTo) {
        return bookingRepository.getBookingsByDate(dateNow,dateTo);
    }

    @Override
    public int getBookingsByMonth(int month) {
        return bookingRepository.getBookingsByMonth(month);
    }

    @Override
    public int getBookingsByYear(int year) {
        return bookingRepository.getBookingsByYear(year);
    }


    @Override
    public Booking saveOrUpdate(Booking booking) {
        return bookingRepository.save(booking);
    }

//    @Override
//    public void saveDetail(String bookingId, String carId) {
//        bookingRepository.saveDetail(bookingId, carId);
//    }

}