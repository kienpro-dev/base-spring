package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Page<Car> findByUserOwn(Pageable pageable, User userOwn);

    @Query(value = "SELECT * FROM cars WHERE name LIKE %?1%", nativeQuery = true)
    Page<Car> findAllByNameLike(String keyword, Pageable pageable);
    List<Car> findByFuelType(String fuelType);

    @Query(value = "SELECT DISTINCT c.* " +
            "FROM cars c " +
            "LEFT JOIN booking_car bc ON c.id = bc.car_id " +
            "LEFT JOIN bookings b ON bc.booking_id = b.id " +
            "LEFT JOIN ( " +
            "    SELECT bc.car_id, MAX(b.end_date) as latest_end_date " +
            "    FROM booking_car bc " +
            "    JOIN bookings b ON bc.booking_id = b.id " +
            "    GROUP BY bc.car_id " +
            ") latest_booking ON c.id = latest_booking.car_id " +
            "WHERE c.address LIKE %:address% " +
            "AND (latest_booking.latest_end_date IS NULL OR " +
            "     (latest_booking.latest_end_date <= :startDateTime OR b.start_date >= :endDateTime)) " +
            "AND c.name LIKE %:keyword% " +
            "AND c.status_car != 'stopped'",
            countQuery = "SELECT COUNT(DISTINCT c.id) " +
                    "FROM cars c " +
                    "LEFT JOIN booking_car bc ON c.id = bc.car_id " +
                    "LEFT JOIN bookings b ON bc.booking_id = b.id " +
                    "LEFT JOIN ( " +
                    "    SELECT bc.car_id, MAX(b.end_date) as latest_end_date " +
                    "    FROM booking_car bc " +
                    "    JOIN bookings b ON bc.booking_id = b.id " +
                    "    GROUP BY bc.car_id " +
                    ") latest_booking ON c.id = latest_booking.car_id " +
                    "WHERE c.address LIKE %:address% " +
                    "AND (latest_booking.latest_end_date IS NULL OR " +
                    "     (latest_booking.latest_end_date <= :startDateTime OR b.start_date >= :endDateTime)) " +
                    "AND c.name LIKE %:keyword% " +
                    "AND c.status_car != 'stopped'",
            nativeQuery = true)
    Page<Car> findAvailableCars(@Param("startDateTime") LocalDateTime startDateTime,
                                @Param("endDateTime") LocalDateTime endDateTime,
                                @Param("address") String address,
                                @Param("keyword") String keyword,
                                Pageable pageable);


    @Query(value = "SELECT DISTINCT c.* " +
            "FROM cars c " +
            "LEFT JOIN booking_car bc ON c.id = bc.car_id " +
            "LEFT JOIN bookings b ON bc.booking_id = b.id " +
            "LEFT JOIN ( " +
            "    SELECT bc.car_id, MAX(b.end_date) as latest_end_date " +
            "    FROM booking_car bc " +
            "    JOIN bookings b ON bc.booking_id = b.id " +
            "    GROUP BY bc.car_id " +
            ") latest_booking ON c.id = latest_booking.car_id " +
            "WHERE c.fuel_type = :fuelType " +
            "AND (latest_booking.latest_end_date IS NULL OR latest_booking.latest_end_date < :dateNow) " +
            "AND c.status_car != 'stopped'",
            nativeQuery = true)
    List<Car> findAvailableCarsByFuelType(@Param("fuelType") String fuelType,
                                          @Param("dateNow") LocalDateTime dateNow);

    @Query(value = "SELECT * FROM cars c INNER JOIN booking_cars ON c.id = bc.car_id WHERE bc.booking_id = :id", nativeQuery = true)
    List<Car> findCarByBookingId(@Param("id")String bookingId);
}
