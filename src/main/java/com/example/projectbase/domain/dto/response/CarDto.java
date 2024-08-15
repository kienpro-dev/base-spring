package com.example.projectbase.domain.dto.response;

import com.example.projectbase.constant.StatusEnum;
import com.example.projectbase.domain.dto.common.DateAuditingDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarDto extends DateAuditingDto {
    private String id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Brand is required")
    private String brand;

    @NotNull(message = "Model is required")
    private String model;
    @NotNull(message = "Color is required")
    private String color;

    @Min(value = 1 , message = "Number of seats must be greater than 1")
    private Integer numberOfSeats;

    @Min(value = 1885, message = "Production year must be greater than 1885")
    private Integer productionYear;

    @NotNull(message = "Transmission type is required")
    private String transmissionType;
    @NotNull(message = "Fuel type is required")
    private String fuelType;

    @NotNull(message = "Mileage is required")
    private Double mileage;

    @DecimalMin(value = "0.0", inclusive = false , message = "fuel consumption must be greater than 0.0")
    private Double fuelConsumption;

    @DecimalMin(value = "0.0", inclusive = false , message = "base price must be greater than 0.0")
    private Double basePrice;
    @NotNull(message = "Deposit is required")
    private Double deposit;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Deposit is required")
    private String description;

    private String additionalFunctions;

    @NotNull(message = "Term of use is required")
    private String termOfUse;
    private Document document;
    private List<Image> images;
    private User userOwn;
    private List<Booking> bookings;
    private boolean available;
    private StatusEnum statusCar;
}
