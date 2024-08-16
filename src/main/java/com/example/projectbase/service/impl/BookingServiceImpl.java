package com.example.projectbase.service.impl;


import com.example.projectbase.domain.entity.Booking;

import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Booking> getBookingByCarId(String carId) {
        return bookingRepository.findByCarId(carId);
    }

    @Override
    public List<Booking> getBookingByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking getBookingById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public List<Booking> getBookingByCarOwnerId(String id) {
        return bookingRepository.getBookingByCarOwnerId(id);
    }

//    @Override
//    public void saveDetail(String bookingId, String carId) {
//        bookingRepository.saveDetail(bookingId, carId);
//    }

}