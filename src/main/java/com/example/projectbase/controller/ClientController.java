package com.example.projectbase.controller;

import com.example.projectbase.base.CarMvc;
import com.example.projectbase.domain.entity.Car;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.CarService;
import com.example.projectbase.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CarMvc
@RequiredArgsConstructor
public class ClientController {
    private final CarService carService;

    private final SessionService sessionService;

    @GetMapping(value = "/client")
	public String getPage(Model model, @RequestParam(name = "field") Optional<String> field,
						  @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
						  @RequestParam(name = "keywords", defaultValue = "") Optional<String> keywords,
						  @CurrentUser UserPrincipal userPrincipal
						  ) {
		model.addAttribute("currentUser",userPrincipal);
		String keyword = keywords.orElse(sessionService.get("keywords"));
		sessionService.set("keywords", keyword);
		Sort sort = Sort.by(Sort.Direction.ASC, field.orElse("name"));
		Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(12), sort);
		Page<Car> resultPage = carService.findAllByNameLike(keyword, pageable);

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

		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("field", field.orElse("id"));
		model.addAttribute("size", size.orElse(10));
		model.addAttribute("keywords", keyword);
		model.addAttribute("resultPage", resultPage);
		return "client/client/client";
	}
}
