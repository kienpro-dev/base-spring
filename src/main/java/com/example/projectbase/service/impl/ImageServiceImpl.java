package com.example.projectbase.service.impl;

import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.repository.CarRepository;
import com.example.projectbase.repository.ImageRepository;
import com.example.projectbase.service.CloudinaryService;
import com.example.projectbase.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final CarRepository carRepository;

    @Override
    public List<Image> handleSaveImage(String carId, MultipartFile[] files) {
        Car car = this.carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car not found"));
        List<String> urls = cloudinaryService.uploadImages(files);
        List<Image> images = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image();
            image.setUrl(url);
            image.setCar(car);
            images.add(image);
        }
        return imageRepository.saveAll(images);
    }

    @Override
    @Transactional
    public void deleteImagesByCarId(String carId) {
        this.imageRepository.deleteAllByCarId2(carId);
    }

    @Override
    public List<Image> findAllByCarId(String carId) {
        return this.imageRepository.findAllByCarId(carId);
    }
}
