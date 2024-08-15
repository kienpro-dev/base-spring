package com.example.projectbase.controller.client;

import com.example.projectbase.constant.StatusEnum;
import com.example.projectbase.domain.dto.request.ChangeStatus;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.mapper.CarMapper;
import com.example.projectbase.repository.BookingRepository;
import com.example.projectbase.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StatusController {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @PostMapping("/car-owner/my-car/status/{carId}")
    public ResponseEntity<CarDto> updateStatus(@RequestBody ChangeStatus changeStatus,
                                               @PathVariable String carId){
        // available, stopped, booked
        Optional<Car> carOptional = this.carRepository.findById(carId);
        if(carOptional.isPresent()){
            Car car = carOptional.get();
            StatusEnum statusEnum = StatusEnum.valueOf(changeStatus.getStatus());
            car.setStatusCar(statusEnum);
            this.carRepository.save(car);
            return ResponseEntity.ok().body(carMapper.toCarDto(car));
        }
        return ResponseEntity.badRequest().build();
    }
}
