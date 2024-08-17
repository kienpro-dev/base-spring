package com.example.projectbase.repository;

import com.example.projectbase.domain.dto.response.BookingDetailDto;
import com.example.projectbase.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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

    @Query(value = "SELECT * FROM bookings b INNER JOIN booking_car bc ON b.id = bc.booking_id WHERE car_id = :carId AND b.status = 'CONFIRM'", nativeQuery = true)
    List<Booking> findByCarId(@Param("carId")String carId);

    @Query(value = "SELECT * FROM bookings b WHERE b.user_id = :userId", nativeQuery = true)
    List<Booking> findByUserId(@Param("userId")String userId);

    @Query(value = "SELECT * FROM bookings b JOIN booking_car bc " +
            "ON b.id = bc.booking_id JOIN cars c ON bc.car_id = c.id " +
            "JOIN users u ON c.user_id = u.id WHERE u.id = :id", nativeQuery = true)
    List<Booking> getBookingByCarOwnerId(@Param("id")String id);

    @Query(value = "SELECT new com.example.projectbase.domain.dto.response.BookingDetailDto(b.id,b.createdDate,b.paymentMethod,b.startDate,b.endDate,b.status,c.name,c.brand,c.userOwn.name,u.address,u.email,u.name,u.phoneNumber) FROM Booking b INNER JOIN b.user u INNER JOIN b.cars c WHERE b.id=?1")
    BookingDetailDto getBookingDetail(String bookingId);




//    @Transactional
//    @Modifying
//    @Query(value = "insert into booking_car values(?1, ?2)", nativeQuery = true)
//    void saveDetail(String bookingId, String carId);

}