package com.toms.app.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;


@Controller
@RequestMapping("/admin")
public class AdminDashboardControllerImpl implements AdminDashboardController {

    private final ItemService itemService;

    public AdminDashboardControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @GetMapping
    public ModelAndView adminDashboard() {
        List<ItemDTO> latestItems = itemService.getAllItems().stream()
            .sorted(Comparator.comparing(ItemDTO::getCreatedAt).reversed())
            .limit(5)
            .toList();
        ModelAndView mav = new ModelAndView("admin");
        mav.addObject("items", latestItems);
        return mav;
    }
    
}