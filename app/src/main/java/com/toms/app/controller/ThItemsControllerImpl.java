package com.toms.app.controller;

import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ThItemsControllerImpl implements ThItemsController {

    ItemService itemService;

    public ThItemsControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/produkte")
    public ModelAndView products() {

        List<ItemDTO> items = this.itemService.getAllItems();

        ModelAndView mav = new ModelAndView("produkte");
        mav.addObject("items", items);
        return mav;
    }

}
