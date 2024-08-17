package com.example.projectbase.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailDto {
    private String bookingId;
    private LocalDateTime dateOrder;
    private String paymentMethod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String carName;
    private String brand;
    private String carOwner;
    private String addCustomer;
    private String emailCustomer;
    private String nameCustomer;
    private String phoneNumber;
    private Double total;

    public BookingDetailDto(String bookingId, LocalDateTime dateOrder, String paymentMethod, LocalDateTime startDate, LocalDateTime endDate, String status, String carName, String brand, String carOwner, String addCustomer, String emailCustomer, String nameCustomer, String phoneNumber) {
        this.bookingId = bookingId;
        this.dateOrder = dateOrder;
        this.paymentMethod = paymentMethod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.carName = carName;
        this.brand = brand;
        this.carOwner = carOwner;
        this.addCustomer = addCustomer;
        this.emailCustomer = emailCustomer;
        this.nameCustomer = nameCustomer;
        this.phoneNumber = phoneNumber;
    }
}
