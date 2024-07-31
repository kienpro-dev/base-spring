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
    public PaginationResponseDto<CarDto> getCars(PaginationFullRequestDto request) {
        Pageable pageable = PaginationUtil.buildPageable(request, SortByDataConstant.CAR);
        Page<Car> carPage = this.carRepository.findAll(pageable);
        List<CarDto> carDtos = carMapper.toCarDtos(carPage.getContent());
        PagingMeta pagingMeta = new PagingMeta(carPage.getTotalElements(), carPage.getSize(), carPage.getNumber(), carPage.getTotalPages(), SortByDataConstant.CAR.getSortBy("name"), CommonConstant.SORT_TYPE_DESC);
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
        String registrationFile = carCreateDTO.getDocument().getRegistrationUrl();
        String insuranceFile = carCreateDTO.getDocument().getInsuranceUrl();
        String certificateFile = carCreateDTO.getDocument().getCertificateUrl();

        Document document = Document.builder()
                .certificateUrl(certificateFile)
                .insuranceUrl(insuranceFile)
                .registrationUrl(registrationFile)
                .build();

        List<String> images = carCreateDTO.getImages();
        List<Image> imageList = new ArrayList<>();
        for(int i = 0; i< images.size(); i++){
            Image im = new Image();
            im.setUrl(images.get(i));
            imageList.add(im);
        }

        Optional<User> userOptional = this.userRepository.findById("25bdbc83-c45c-4d07-af86-40e18680ff13");
        User userOwn = userOptional.get();
        carCreateDTO.setUserOwn(userOwn);

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
        newCar.setUserOwn(userOwn);


        Car savedCar = this.carRepository.save(newCar);
        System.out.println(newCar.getUserOwn().getName());
        System.out.println(savedCar.getUserOwn().getName());
        for(Image image: imageList){
            image.setCar(savedCar);
        }
        this.imageRepository.saveAll(imageList);
        return carMapper.toCarDto(savedCar);
    }

    @Override
    public CarDto handleUpdateCar(CarDto carDto) {
        Optional<Car> carOptional = this.carRepository.findById(carDto.getId());
        if(carOptional.isPresent()){
            Car currentCar = carOptional.get();
            this.imageService.deleteImagesByCarId(currentCar.getId());
            List<Image> imageList = carDto.getImages();
            if(imageList != null){
                for(Image image: imageList){
                    image.setCar(currentCar);
                }
            }

            Optional<User> userOptional = this.userRepository.findById("25bdbc83-c45c-4d07-af86-40e18680ff13");
            User userOwn = userOptional.get();


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
            currentCar.setUserOwn(userOwn);
            this.carRepository.save(currentCar);
            this.imageRepository.saveAll(imageList);
            return carMapper.toCarDto(currentCar);
        }
        return null;
    }

    @Override
    public Optional<Car> findById(String id) {
        Optional<Car> car = this.carRepository.findById(id);
        return car;
    }

}
