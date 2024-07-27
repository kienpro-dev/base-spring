package com.example.projectbase.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UserUpdateRequestDto {

    private String name;

    private LocalDateTime dateOfBirth;

    private String nationalId;

    private String email;

    private String phoneNumber;

    private String address;

    private String drivingLicense;
}
