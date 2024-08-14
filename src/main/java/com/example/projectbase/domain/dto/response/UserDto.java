package com.example.projectbase.domain.dto.response;

import com.example.projectbase.domain.dto.common.DateAuditingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private String phoneNumber;

    private String drivingLicense;

    private Double balance;

    private boolean isActive;


}

