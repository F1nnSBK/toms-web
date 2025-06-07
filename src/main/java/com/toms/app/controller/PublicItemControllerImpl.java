package com.toms.app.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;

@RestController
@RequestMapping("/api/v1/public")
public class PublicItemControllerImpl implements PublicItemController {
    
    ItemService itemService;

    public PublicItemControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @GetMapping("/products")
    public List<ItemDTO> getAllItems() {
        return this.itemService.getAllItems();
    }

}
