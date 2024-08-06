package com.example.projectbase.domain.dto.request;

import com.example.projectbase.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String id;
    private String name;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationalId;

    public UserRequestDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.dateOfBirth = formatDate(user.getDateOfBirth());
        this.nationalId = user.getNationalId();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
    }

    public static String formatDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getDateOfBirth() {
        return convertToLocalDateTime(this.dateOfBirth);
    }
}

