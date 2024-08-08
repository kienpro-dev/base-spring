package com.example.projectbase.domain.dto.response;

import com.example.projectbase.domain.dto.common.DateAuditingDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarDto extends DateAuditingDto {
    private String id;
    private String name;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private Integer numberOfSeats;
    private Integer productionYear;
    private String transmissionType;
    private String fuelType;
    private Double mileage;
    private Double fuelConsumption;
    private Double basePrice;
    private Double deposit;
    private String address;
    private String description;
    private String additionalFunctions;
    private String termOfUse;
    private Document document;
    private List<Image> images;
    private User userOwn;
    private List<Booking> bookings;
}
