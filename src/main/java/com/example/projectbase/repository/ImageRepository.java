package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    void deleteAllByCarId(String carId);
    List<Image> findAllByCarId(String carId);
}
