package com.example.projectbase.service;

import com.example.projectbase.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<Image> handleSaveImage(String carId, MultipartFile[] files);

    void deleteImagesByCarId(String carId);
    List<Image> findAllByCarId(String carId);
}
