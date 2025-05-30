package com.toms.app.controller;

import com.toms.app.dto.ItemDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ItemController {

    @GetMapping("/")
    List<ItemDTO> getAllItems();

    @GetMapping("/{itemId}")
    ItemDTO getItemById(@PathVariable("itemId") Long id);

    @PutMapping("/{itemId}")
    ItemDTO updateItem(@PathVariable("itemId") Long id,
                       @Valid @RequestBody ItemDTO item);

    @DeleteMapping("/{itemId}")
    void deleteItemById(@PathVariable("itemId") Long id);

    @PostMapping("/")
    ItemDTO addItem(@Valid @RequestBody ItemDTO item);
}
