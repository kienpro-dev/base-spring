package com.example.projectbase.controller.client;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.Image;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("/car/add")
    public String addCar(Model model, @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto){
        return "client/carOwner/createcar";
    }

    @PostMapping("/car/add")
    public String addNewCar(Model model, @Valid @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto,
                            BindingResult result,
                            @RequestParam(name = "registrationPaper", required = false) MultipartFile papers,
                            @RequestParam(name = "certificates", required = false) MultipartFile cers,
                            @RequestParam(name = "insurance", required = false) MultipartFile ins,
                            @RequestParam(name = "carImages") MultipartFile[] carImages,
                            @CurrentUser UserPrincipal userPrincipal){
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

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
//            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//            User userOwn = this.userService.findById(userPrincipal.getId());
//            carCreateDto.setUserOwn(userOwn);
//        }
//
        User userOwn = this.userService.findById(userPrincipal.getId());
        carCreateDto.setUserOwn(userOwn);


        carCreateDto.setDocument(document);
        CarDto carDto = this.carService.handleSaveCar(carCreateDto);

        return "redirect:/car-owner/my-car";
    }

    @GetMapping("/my-car")
    public String viewCar(Model model,
                          @RequestParam(name = "keyword" , required = false) String keyword,
                          @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(name = "size", required = false, defaultValue = "4") int size,
                          @RequestParam(name = "sortBy", required = false, defaultValue = "createdDate") String sortBy,
                          @RequestParam(name = "isAscending", required = false, defaultValue = "true") boolean isAscending,
                          @CurrentUser UserPrincipal userPrincipal) {
        PaginationFullRequestDto requestDto = new PaginationFullRequestDto();
        requestDto.setKeyword(keyword);
        requestDto.setPageNum(page);
        requestDto.setPageSize(size);
        requestDto.setSortBy(SortByDataConstant.CAR.getSortBy(sortBy));
        requestDto.setIsAscending(isAscending);

        User userOwn = this.userService.findById(userPrincipal.getId());

        PaginationResponseDto<CarDto> carPage = this.carService.getCars(requestDto, userOwn);
        List<CarDto> cars = carPage.getItems();
        model.addAttribute("cars", cars);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getMeta().getTotalPages());
        return "client/carOwner/mycar";
    }

    @GetMapping("/my-car/{id}")
    public String viewDetails(Model model, @PathVariable String id){
        CarDto carDto = this.carService.getCarById(id);
        List<Booking> bookings = carDto.getBookings();
        Booking lastBooking = null;
        if (!bookings.isEmpty()) {
            lastBooking = bookings.get(bookings.size() - 1);
        }
        model.addAttribute("lastBooking", lastBooking);
        model.addAttribute("carDto", carDto);
        model.addAttribute("id", id);
        return "client/carOwner/details";
    }

    @PostMapping("/my-car/upadte")
    public String upadteCar(Model model,
                            @ModelAttribute("carDto") @Valid CarDto carDto,
                            BindingResult result,
                            @RequestParam(name = "carImages", required = false) MultipartFile[] carImages){
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getField() + " - " + error.getDefaultMessage());
        }

        if (result.hasErrors()) {
            return "client/carOwner/mycar";
        }
        CarDto carX = this.carService.getCarById(carDto.getId());
        model.addAttribute("carDto", carX);

        if(carImages != null && carImages.length > 0){
            List<MultipartFile> nonEmptyFiles = new ArrayList<>();
            for (MultipartFile file : carImages) {
                if (!file.isEmpty()) {
                    nonEmptyFiles.add(file);
                }
            }
            if (!nonEmptyFiles.isEmpty()) {
                List<String> urls = this.cloudinaryService.uploadImages(nonEmptyFiles.toArray(new MultipartFile[0]));
                List<Image> imageList = new ArrayList<>();
                for(String url : urls){
                    Image image = new Image();
                    image.setUrl(url);
                    imageList.add(image);
                }
                carDto.setImages(imageList);
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User userOwn = this.userService.findById(userPrincipal.getId());
            carDto.setUserOwn(userOwn);
        }


        CarDto car = this.carService.handleUpdateCar(carDto);

        return "redirect:/car-owner/my-car";
    }

}
