package com.example.projectbase.service.impl;


import com.example.projectbase.domain.dto.response.BookingDetailDto;
import com.example.projectbase.domain.entity.Booking;

import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public BookingDetailDto getBookingDetail(String id) {
        BookingDetailDto bookingDetail=bookingRepository.getBookingDetail(id);
        Optional<Booking> booking=bookingRepository.findById(bookingDetail.getBookingId());
        bookingDetail.setTotal(booking.get().getTotal());
        return bookingDetail;
    }

//    @Override
//    public void saveDetail(String bookingId, String carId) {
//        bookingRepository.saveDetail(bookingId, carId);
//    }

}