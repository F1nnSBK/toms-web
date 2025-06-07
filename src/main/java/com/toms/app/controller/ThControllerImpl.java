package com.toms.app.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;


@Controller
public class ThControllerImpl implements ThController {

    ItemService itemService;

    public ThControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    @Override
    public ModelAndView home() {
        List<ItemDTO> featuredItems = this.itemService.getFeaturedItems();
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("featuredItems", featuredItems);
        mav.addObject("requestURI", "/");
        return mav;
    }

    @GetMapping("/produkte")
    public ModelAndView products() {

        List<ItemDTO> items = this.itemService.getAllItems();

        ModelAndView mav = new ModelAndView("produkte");
        mav.addObject("items", items);
        mav.addObject("requestURI", "/produkte");
        return mav;
    }

    @GetMapping("/kontakt")
    public ModelAndView contact() {
        ModelAndView mav = new ModelAndView("kontakt");
        mav.addObject("requestURI", "/kontakt");
        return mav;
    }

    @GetMapping("/ueber-mich")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("ueber-mich");
        mav.addObject("requestURI", "/ueber-mich");
        return mav;
    }

    @GetMapping("/produkte/{productId}")
    public ModelAndView getMethodName(@PathVariable Long productId) {
        ModelAndView mav = new ModelAndView("produkt");
        mav.addObject("item", itemService.getItemById(productId));
        return mav;
    }
    


}
