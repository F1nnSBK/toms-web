package com.toms.app.controller;

import com.toms.app.model.Item;
import com.toms.app.repository.ItemRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminDashboardControllerImpl {

    private final ItemRepository itemRepository;

    public AdminDashboardControllerImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        List<Item> latestItems = itemRepository.findAll(PageRequest.of(0, 5, Sort.by("createdAt").descending())).getContent();
        model.addAttribute("items", latestItems);

        return "admin";
    }

    @PostMapping("/items/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        try {
            itemRepository.deleteById(id);
        } catch (NoSuchElementException nse) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nse.getMessage(), nse);
        }
        return "redirect:/admin";
    }

}