package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    @Modifying
    @Query("DELETE FROM Image i WHERE i.car.id = ?1")
    void deleteAllByCarId2(String carId);

    List<Image> findAllByCarId(String carId);

}
