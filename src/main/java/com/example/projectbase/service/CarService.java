package com.example.projectbase.service;

import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;

import java.util.Optional;

public interface CarService {
    CarDto getCarById(String carId);
    PaginationResponseDto<CarDto> getCars(PaginationFullRequestDto request, User userOwn);
    Optional<Car> findById(Car car);
    void deleteById(String id);
    CarDto handleSaveCar(CarCreateDTO carCreateDTO);
    CarDto handleUpdateCar(CarDto carDto);
    Optional<Car> findById(String id);
}
