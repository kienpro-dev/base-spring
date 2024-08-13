package com.example.projectbase.repository;

import com.example.projectbase.domain.dto.response.CarResponseDto;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,String> {
    Page<Car> findByUserOwn(Pageable pageable, User userOwn);

    Page<Car> findAllByNameLike(Pageable pageable,String keyword);

    @Query("select new com.example.projectbase.domain.dto.response.CarResponseDto(c.id,c.name,c.numberOfSeats,c.brand,c.color,c.userOwn.name) from Car c  where (:keywords is null or :keywords = '' or c.name like %:keywords%) ")
    Page<CarResponseDto> findAllCars(Pageable pageable, @Param("keywords")String keywords);


}
