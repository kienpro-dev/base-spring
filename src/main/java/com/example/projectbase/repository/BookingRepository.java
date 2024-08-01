package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query(value = "SELECT TOP 1" +
            " b.* FROM bookings b " +
            "INNER JOIN booking_car bc ON b.id = bc.booking_id " +
            "INNER JOIN cars c ON bc.car_id = c.id " +
            "WHERE c.id = ?1 " +
            "GROUP BY c.id "+
            "ORDER BY b.start_date DESC "
            , nativeQuery = true)
    Booking findCarBooking(String carId);
}
