package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Page<Car> findByUserOwn(Pageable pageable, User userOwn);

    @Query(value = "SELECT * FROM cars WHERE name LIKE %?1%", nativeQuery = true)
    Page<Car> findAllByNameLike(String keyword, Pageable pageable);

    List<Car> findByFuelType(String fuelType);
}
