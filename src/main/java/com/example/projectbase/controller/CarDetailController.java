package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.domain.dto.response.CarDto;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CarMvc
@RequiredArgsConstructor
public class CarDetailController {
    private final CarService carService;

    private final ImageService imageService;

    @GetMapping("/product-detail")
    public String viewDetails(Model model, @RequestParam(name = "id") String id){
        CarDto carDto = this.carService.getCarById(id);
        List<Booking> bookings = carDto.getBookings();
        Booking lastBooking = null;
        if (!bookings.isEmpty()) {
            lastBooking = bookings.get(bookings.size() - 1);
        }
        model.addAttribute("lastBooking", lastBooking);
        model.addAttribute("carDto", carDto);
        model.addAttribute("id", id);
        return "client/productDetail/details";
    }
}
