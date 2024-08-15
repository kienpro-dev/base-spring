package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

//    Car toCar(CarCreateDTO carCreateDto);

    @Mappings({
            @Mapping(target = "id", source = "car.id"),
            @Mapping(target = "name", source = "car.name"),
            @Mapping(target = "licensePlate", source = "car.licensePlate"),
            @Mapping(target = "brand", source = "car.brand"),
            @Mapping(target = "model", source = "car.model"),
            @Mapping(target = "color", source = "car.color"),
            @Mapping(target = "numberOfSeats", source = "car.numberOfSeats"),
            @Mapping(target = "productionYear", source = "car.productionYear"),
            @Mapping(target = "transmissionType", source = "car.transmissionType"),
            @Mapping(target = "fuelType", source = "car.fuelType"),
            @Mapping(target = "mileage", source = "car.mileage"),
            @Mapping(target = "fuelConsumption", source = "car.fuelConsumption"),
            @Mapping(target = "basePrice", source = "car.basePrice"),
            @Mapping(target = "deposit", source = "car.deposit"),
            @Mapping(target = "address", source = "car.address"),
            @Mapping(target = "description", source = "car.description"),
            @Mapping(target = "additionalFunctions", source = "car.additionalFunctions"),
            @Mapping(target = "termOfUse", source = "car.termOfUse"),
            @Mapping(target = "images", source = "car.images"),
            @Mapping(target = "document", source = "car.document"),
            @Mapping(target = "bookings", source = "car.bookings"),
            @Mapping(target= "userOwn", source = "car.userOwn"),
            @Mapping(target = "available", source = "car.available"),
            @Mapping(target = "statusCar", source = "car.statusCar")
    })
    CarDto toCarDto(Car car);

    @Mappings({
            @Mapping(target = "id", source = "car.id"),
            @Mapping(target = "name", source = "car.name"),
            @Mapping(target = "licensePlate", source = "car.licensePlate"),
            @Mapping(target = "brand", source = "car.brand"),
            @Mapping(target = "model", source = "car.model"),
            @Mapping(target = "color", source = "car.color"),
            @Mapping(target = "numberOfSeats", source = "car.numberOfSeats"),
            @Mapping(target = "productionYear", source = "car.productionYear"),
            @Mapping(target = "transmissionType", source = "car.transmissionType"),
            @Mapping(target = "fuelType", source = "car.fuelType"),
            @Mapping(target = "mileage", source = "car.mileage"),
            @Mapping(target = "fuelConsumption", source = "car.fuelConsumption"),
            @Mapping(target = "basePrice", source = "car.basePrice"),
            @Mapping(target = "deposit", source = "car.deposit"),
            @Mapping(target = "address", source = "car.address"),
            @Mapping(target = "description", source = "car.description"),
            @Mapping(target = "additionalFunctions", source = "car.additionalFunctions"),
            @Mapping(target = "termOfUse", source = "car.termOfUse"),
            @Mapping(target = "images", source = "car.images"),
            @Mapping(target = "document", source = "car.document"),
            @Mapping(target = "bookings", source = "car.bookings"),
            @Mapping(target= "userOwn", source = "car.userOwn"),
            @Mapping(target = "statusCar", source = "car.statusCar")
    })
    List<CarDto> toCarDtos(List<Car> car);

}
