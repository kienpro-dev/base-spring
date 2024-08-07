package com.example.projectbase.domain.dto.response;

import com.example.projectbase.domain.dto.common.DateAuditingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto extends DateAuditingDto {

    private String id;

    private String email;

    private String roleName;

    private String name;

    private String nationalId;

    private String address;

    private LocalDateTime dateOfBirth;

    private String phoneNumber;

    private String drivingLicense;

    private boolean isActive;


}

