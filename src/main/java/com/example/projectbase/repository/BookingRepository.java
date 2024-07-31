package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    Booking findByCars(List<Car> cars);
}
