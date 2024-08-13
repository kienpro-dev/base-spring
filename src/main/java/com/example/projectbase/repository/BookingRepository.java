package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
//    @Transactional
//    @Modifying
//    @Query(value = "insert into booking_car values(?1, ?2)", nativeQuery = true)
//    void saveDetail(String bookingId, String carId);
}
