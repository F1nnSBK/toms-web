package com.toms.app.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/items")
public class ItemControllerImpl implements ItemController {

    private final ItemService itemService;

    public ItemControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public List<ItemDTO> getAllItems() {
        return this.itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItemById(@PathVariable("itemId") Long id) {
        try {
            return this.itemService.getItemById(id);
        } catch (NoSuchElementException nse) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item by id not found");
        }
    }

    @PutMapping("/{itemId}")
    public ItemDTO updateItem(@PathVariable("itemId") Long id,
                              @Valid @RequestBody ItemDTO item){
        item.setId(id);
        item.setCreatedAt(LocalDateTime.now());
        try {
            return this.itemService.updateItem(item);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException | ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemById(@PathVariable("itemId") Long id) {
        try {
            this.itemService.deleteItemById(id);
        } catch (NoSuchElementException nse) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ItemDTO addItem(@Valid @RequestBody ItemDTO item) {
        item.setCreatedAt(LocalDateTime.now());
        try{
            return this.itemService.addItem(item);
        } catch (ConstraintViolationException cve) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    
}
