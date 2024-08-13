package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query(value = "SELECT "
            + "COUNT(b.id) AS bookings "
            + "FROM bookings b "
            + "WHERE b.created_date >= :dateNow AND b.created_date < :dateTo", nativeQuery = true)
    Integer getBookingsByDate(@Param("dateNow")String dateNow, @Param("dateTo")String dateTo);

    @Query(value = "SELECT "
            + "COUNT(b.id) AS bookings "
            + "FROM bookings b "
            + "WHERE MONTH(b.created_date) = :month", nativeQuery = true)
    Integer getBookingsByMonth(@Param("month")int month);

    @Query(value = "SELECT "
            + "COUNT(b.id) AS bookings "
            + "FROM bookings b "
            + "WHERE YEAR(b.created_date) = :year", nativeQuery = true)
    Integer getBookingsByYear(@Param("year")int year);

}
