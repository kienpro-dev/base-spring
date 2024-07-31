package com.example.projectbase.controller.client;

import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StatusController {

    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    @PostMapping("/car-owner/my-car/status")
    public ResponseEntity<CarDto> updateStatus(@RequestBody CarDto carDto, HttpServletRequest request,
                                               @RequestParam(name = "status") String status){
//        Optional<Car> carOptional = this.carRepository.findById(carDto.getId());
//        if(carOptional.isPresent()){
//            Car car = carOptional.get();
//            List<Car> cars = new ArrayList<>();
//            cars.add(car);
//            Booking booking = this.bookingRepository.findByCars(cars);
//            booking.setStatus(status);
//            List<Booking>
//            car.setBookings(booking);
//            this.bookingRepository.save(booking);
//
//            this.carRepository.save(car);
//        }
        return ResponseEntity.ok().body(carDto);
    }
}
