package com.example.projectbase.controller.client;

import com.example.projectbase.constant.CommonConstant;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.constant.StatusEnum;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.CarCreateDTO;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.*;
import com.example.projectbase.domain.mapper.CarMapper;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/car-owner")
public class CarController {
    private final CarService carService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;
    private final UserService userService;
    private final AuthService authService;
    private final CarMapper carMapper;


    @GetMapping("/car/add")
    public String addCar(Model model, @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto, @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
        }
        List<String> fuelTypes = CommonConstant.fuelType;
        model.addAttribute("fuelTypes", fuelTypes);
        return "client/carOwner/createcar";
    }

    @PostMapping("/car/add")
    public String addNewCar(Model model, @Valid @ModelAttribute("carCreateDto") CarCreateDTO carCreateDto,
                            BindingResult result,
                            @RequestParam(name = "registrationPaper", required = false) MultipartFile papers,
                            @RequestParam(name = "certificates", required = false) MultipartFile cers,
                            @RequestParam(name = "insurance", required = false) MultipartFile ins,
                            @RequestParam(name = "carImages") MultipartFile[] carImages,
                            @CurrentUser UserPrincipal userPrincipal) {
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        if (result.hasErrors()) {
            return "client/carOwner/createcar";
        }

        Document document = new Document();
        if (papers != null) {
            String url = cloudinaryService.uploadImage(papers);
            document.setRegistrationUrl(url);
        }

        if (cers != null) {
            String url = cloudinaryService.uploadImage(cers);
            document.setCertificateUrl(url);
        }

        if (ins != null) {
            String url = cloudinaryService.uploadImage(ins);
            document.setInsuranceUrl(url);
        }

        if (carImages != null) {
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
        carCreateDto.setStatusCar(StatusEnum.available);


        carCreateDto.setDocument(document);
        CarDto carDto = this.carService.handleSaveCar(carCreateDto);

        return "redirect:/car-owner/my-car";
    }

    @GetMapping(value = "/my-car")
    public String list(Model model, @RequestParam(name = "field") Optional<String> field,
                       @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
                       @CurrentUser UserPrincipal userPrincipal) {
        User currentUser = new User();
        if (authService.isAuthenticated()) {
            currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("role", currentUser.getRole().getName());
        }
        Sort sort = Sort.by(Sort.Direction.DESC, field.orElse("createdDate"));
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);
        Page<Car> resultPage = carService.getCars(pageable, currentUser);
        int totalPages = resultPage.getTotalPages();
        int startPage = Math.max(1, page.orElse(1) - 2);
        int endPage = Math.min(page.orElse(1) + 2, totalPages);
        if (totalPages > 5) {
            if (endPage == totalPages)
                startPage = endPage - 5;
            else if (startPage == 1)
                endPage = startPage + 5;
        }
        List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
        List<CarDto> cars = carMapper.toCarDtos(resultPage.getContent());
        model.addAttribute("cars", cars);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("field", field.orElse("createdDate"));
        model.addAttribute("size", size.orElse(5));
        model.addAttribute("resultPage", resultPage);
        return "client/carOwner/mycar";
    }

    @GetMapping("/my-car/{id}")
    public String viewDetails(Model model, @PathVariable String id,
                              @CurrentUser UserPrincipal userPrincipal) {
        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("role", currentUser.getRole().getName());
        }

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
                            @RequestParam(name = "carImages", required = false) MultipartFile[] carImages,
                            @CurrentUser UserPrincipal userPrincipal) {

        if (authService.isAuthenticated()) {
            User currentUser = this.userService.findById(userPrincipal.getId());
            carDto.setUserOwn(currentUser);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("role", currentUser.getRole().getName());
        }

        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        if (result.hasErrors()) {
            return "client/carOwner/mycar";
        }
        CarDto carX = this.carService.getCarById(carDto.getId());

        if (carImages != null && carImages.length > 0) {
            List<MultipartFile> nonEmptyFiles = new ArrayList<>();
            for (MultipartFile file : carImages) {
                if (!file.isEmpty()) {
                    nonEmptyFiles.add(file);
                }
            }
            if (!nonEmptyFiles.isEmpty()) {
                List<String> urls = this.cloudinaryService.uploadImages(nonEmptyFiles.toArray(new MultipartFile[0]));
                List<Image> imageList = new ArrayList<>();
                for (String url : urls) {
                    Image image = new Image();
                    image.setUrl(url);
                    imageList.add(image);
                }
                carDto.setImages(imageList);
            }
        }


        CarDto car = this.carService.handleUpdateCar(carDto);

        return "redirect:/car-owner/my-car";
    }

}
