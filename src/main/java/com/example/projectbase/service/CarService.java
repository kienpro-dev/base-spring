package com.example.projectbase.service;

import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarService {
    CarDto getCarById(String carId);
    PaginationResponseDto<CarDto> getCars(PaginationFullRequestDto request, User userOwn);
    Optional<Car> findById(Car car);
    void deleteById(String id);
    CarDto handleSaveCar(CarCreateDTO carCreateDTO);
    CarDto handleUpdateCar(CarDto carDto);
    Optional<Car> findById(String id);
    List<Car> findAllByFuelType(String fuelType);
    Page<Car> findAllByNameLike(String keyword, Pageable pageable);
    Page<Car> findAvailableCar(String address, LocalDateTime startDateTime, LocalDateTime endDateTime, String keyword, Pageable pageable);
    List<Car> findAvailableCarByfuelType(String fuelType, LocalDateTime dateNow);
}
