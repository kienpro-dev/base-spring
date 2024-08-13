package com.example.projectbase.domain.dto.response;

import com.example.projectbase.domain.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {
    private String id;
    private String name;
    private Integer numberOfSeats;
    private String brand;
    private String color;
    private String owner;

    List<Image> images;

    public CarResponseDto(String id, String name, Integer numberOfSeats, String brand, String color, String owner) {
        this.id = id;
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        this.brand = brand;
        this.color = color;
        this.owner = owner;
    }
}
