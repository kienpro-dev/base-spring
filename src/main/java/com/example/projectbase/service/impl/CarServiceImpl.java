package com.example.projectbase.service.impl;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.pagination.PagingMeta;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.*;
import com.example.projectbase.domain.mapper.CarMapper;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.repository.CarRepository;
import com.example.projectbase.repository.DocumentRepository;
import com.example.projectbase.repository.ImageRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.ImageService;
import com.example.projectbase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final DocumentRepository documentRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public CarDto getCarById(String carId) {
        Car car = this.carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car not found"));
        return carMapper.toCarDto(car);
    }

    @Override
    public PaginationResponseDto<CarDto> getCars(PaginationFullRequestDto request, User userOwn) {
        Pageable pageable = PaginationUtil.buildPageable(request, SortByDataConstant.CAR);
        Page<Car> carPage = this.carRepository.findByUserOwn(pageable, userOwn);
        List<CarDto> carDtos = carMapper.toCarDtos(carPage.getContent());
        PagingMeta pagingMeta = new PagingMeta(carPage.getTotalElements(), carPage.getTotalPages(), carPage.getNumber(), carPage.getSize() , request.getSortBy(), request.getIsAscending().toString());
        return new PaginationResponseDto<>(pagingMeta,carDtos);
    }

    @Override
    public Optional<Car> findById(Car car) {
        Optional<Car> carOptional = this.carRepository.findById(car.getId());
        return carOptional;
    }

    @Override
    public void deleteById(String id) {
        this.carRepository.deleteById(id);
    }

    @Override
    public CarDto handleSaveCar(CarCreateDTO carCreateDTO) {
        Document.DocumentBuilder documentBuilder = Document.builder();

        String registrationFile = carCreateDTO.getDocument() != null ? carCreateDTO.getDocument().getRegistrationUrl() : null;
        if (registrationFile != null) {
            documentBuilder.registrationUrl(registrationFile);
        }

        String insuranceFile = carCreateDTO.getDocument() != null ? carCreateDTO.getDocument().getInsuranceUrl() : null;
        if (insuranceFile != null) {
            documentBuilder.insuranceUrl(insuranceFile);
        }

        String certificateFile = carCreateDTO.getDocument() != null ? carCreateDTO.getDocument().getCertificateUrl() : null;
        if (certificateFile != null) {
            documentBuilder.certificateUrl(certificateFile);
        }

        Document document = documentBuilder.build();

        List<String> images = carCreateDTO.getImages();
        List<Image> imageList = new ArrayList<>();
        for(int i = 0; i< images.size(); i++){
            Image im = new Image();
            im.setUrl(images.get(i));
            imageList.add(im);
        }

        Car newCar = new Car();
        newCar.setName(carCreateDTO.getName());
        newCar.setLicensePlate(carCreateDTO.getLicensePlate());
        newCar.setBrand(carCreateDTO.getBrand());
        newCar.setModel(carCreateDTO.getModel());
        newCar.setColor(carCreateDTO.getColor());
        newCar.setNumberOfSeats(carCreateDTO.getNumberOfSeats());
        newCar.setProductionYear(carCreateDTO.getProductionYear());
        newCar.setTransmissionType(carCreateDTO.getTransmissionType());
        newCar.setFuelType(carCreateDTO.getFuelType());
        newCar.setMileage(carCreateDTO.getMileage());
        newCar.setFuelConsumption(carCreateDTO.getFuelConsumption());
        newCar.setBasePrice(carCreateDTO.getBasePrice());
        newCar.setDeposit(carCreateDTO.getDeposit());
        newCar.setAddress(carCreateDTO.getAddress());
        newCar.setDescription(carCreateDTO.getDescription());
        newCar.setAdditionalFunctions(carCreateDTO.getAdditionalFunctions());
        newCar.setTermOfUse(carCreateDTO.getTermOfUse());
        newCar.setDocument(document);
        newCar.setImages(imageList);
        newCar.setUserOwn(carCreateDTO.getUserOwn());


        Car savedCar = this.carRepository.save(newCar);
        System.out.println(newCar.getUserOwn().getName());
        System.out.println(savedCar.getUserOwn().getName());
        for(Image image: imageList){
            image.setCar(savedCar);
        }
        document.setCar(savedCar);
        this.documentRepository.save(document);
        this.imageRepository.saveAll(imageList);
        return carMapper.toCarDto(savedCar);
    }

    @Override
    public CarDto handleUpdateCar(CarDto carDto) {
        Optional<Car> carOptional = this.carRepository.findById(carDto.getId());
        if(carOptional.isPresent()){
            Car currentCar = carOptional.get();

            List<Image> imageList = carDto.getImages();
            if(imageList != null && !imageList.isEmpty()){
                this.imageService.deleteImagesByCarId(currentCar.getId());
                currentCar.getImages().clear();
                for(Image image: imageList){
                    image.setCar(currentCar);
                }
                currentCar.setImages(imageList);
                this.carRepository.save(currentCar);
            }

            currentCar.setName(carDto.getName());
            currentCar.setLicensePlate(carDto.getLicensePlate());
            currentCar.setBrand(carDto.getBrand());
            currentCar.setModel(carDto.getModel());
            currentCar.setColor(carDto.getColor());
            currentCar.setNumberOfSeats(carDto.getNumberOfSeats());
            currentCar.setProductionYear(carDto.getProductionYear());
            currentCar.setTransmissionType(carDto.getTransmissionType());
            currentCar.setFuelType(carDto.getFuelType());
            currentCar.setMileage(carDto.getMileage());
            currentCar.setFuelConsumption(carDto.getFuelConsumption());
            currentCar.setBasePrice(carDto.getBasePrice());
            currentCar.setDeposit(carDto.getDeposit());
            currentCar.setAddress(carDto.getAddress());
            currentCar.setDescription(carDto.getDescription());
            currentCar.setAdditionalFunctions(carDto.getAdditionalFunctions());
            currentCar.setTermOfUse(carDto.getTermOfUse());
            currentCar.setDocument(carDto.getDocument());
            currentCar.setImages(imageList);
            currentCar.setUserOwn(carDto.getUserOwn());
            this.carRepository.save(currentCar);
            if(imageList != null){
                this.imageRepository.saveAll(imageList);
            }
            return carMapper.toCarDto(currentCar);
        }
        return null;
    }

    @Override
    public Optional<Car> findById(String id) {
        Optional<Car> car = this.carRepository.findById(id);
        return car;
    }

    @Override
    public List<Car> findAllByFuelType(String fuelType) {
        return carRepository.findByFuelType(fuelType);
    }

}
