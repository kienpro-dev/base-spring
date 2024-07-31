package com.example.projectbase.controller.client;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/car-owner")
public class CarController {
    private final CarService carService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/car/add")
    public String addCar(Model model, @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto){
        return "client/carOwner/createcar";
    }

    @PostMapping("/car/add")
    public String addNewCar(Model model, @Valid @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto,
                            BindingResult result,
                            @RequestParam(name = "registrationPaper") MultipartFile papers,
                            @RequestParam(name = "certificates") MultipartFile cers,
                            @RequestParam(name = "insurance") MultipartFile ins,
                            @RequestParam(name = "carImages") MultipartFile[] carImages){
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getField() + " - " + error.getDefaultMessage());
        }

        if (result.hasErrors()) {
            return "client/carOwner/createcar";
        }
        Document document = new Document();
        if(papers != null){
            String url = cloudinaryService.uploadImage(papers);
            document.setRegistrationUrl(url);
        }

        if(cers != null){
            String url = cloudinaryService.uploadImage(cers);
            document.setCertificateUrl(url);
        }

        if(ins != null){
            String url = cloudinaryService.uploadImage(ins);
            document.setInsuranceUrl(url);
        }

        if(carImages != null){
            List<String> urls = cloudinaryService.uploadImages(carImages);
            carCreateDto.setImages(urls);
        }

        carCreateDto.setDocument(document);
        CarDto carDto = this.carService.handleSaveCar(carCreateDto);

        return "redirect:/car-owner/my-car";
    }

    @GetMapping("/my-car")
    public String viewCar(Model model,
                          @RequestParam(name = "keyword" , required = false) String keyword,
                          @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(name = "size", required = false, defaultValue = "4") int size,
                          @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                          @RequestParam(name = "isAscending", required = false, defaultValue = "true") boolean isAscending) {
        PaginationFullRequestDto requestDto = new PaginationFullRequestDto();
        requestDto.setKeyword(keyword);
        requestDto.setPageNum(page - 1);
        requestDto.setPageSize(size);
        requestDto.setSortBy(sortBy);
        requestDto.setIsAscending(isAscending);

        PaginationResponseDto<CarDto> carPage = this.carService.getCars(requestDto);
        List<CarDto> cars = carPage.getItems();
        model.addAttribute("cars", cars);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getMeta().getTotalPages());
        return "client/carOwner/mycar";
    }

    @GetMapping("/my-car/{id}")
    public String viewDetails(Model model, @PathVariable String id){
        CarDto carDto = this.carService.getCarById(id);
        model.addAttribute("carDto", carDto);
        model.addAttribute("id", id);
        return "client/carOwner/details";
    }

    @PostMapping("/my-car/upadte")
    public String upadteCar(Model model,
                            @ModelAttribute("carDto") @Valid CarDto carDto,
                            BindingResult result,
                            @RequestParam(name = "carImages") MultipartFile[] carImages){
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getField() + " - " + error.getDefaultMessage());
        }

        if (result.hasErrors()) {
            return "client/carOwner/mycar";
        }
        CarDto carX = this.carService.getCarById(carDto.getId());
        model.addAttribute("carDto", carX);

        if(carImages != null){
            List<String> urls = cloudinaryService.uploadImages(carImages);
            List<Image> imageList = new ArrayList<>();
            for(String url : urls){
                Image image = new Image();
                image.setUrl(url);
                imageList.add(image);
            }
            carDto.setImages(imageList);
        }


        CarDto car = this.carService.handleUpdateCar(carDto);

        return "redirect:/car-owner/my-car";
    }

}
