package com.toms.app.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/admin/items")
public class ItemManagerControllerImpl implements ItemManagerController {
    
    ItemService itemService;
    private final String itemFormPath = "item-form";
    
    public ItemManagerControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }
        
    @Override
    @GetMapping
    public ModelAndView adminItemManager() {
        List<ItemDTO> allItems = itemService.getAllItems();
        ModelAndView mav = new ModelAndView("itemManager");
        mav.addObject("items", allItems);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView showCreateForm(){
        ModelAndView mav = new ModelAndView(itemFormPath);
        mav.addObject("item", new ItemDTO());
        return mav;
    }

    @GetMapping("/edit/{itemId}")
    public ModelAndView showCreateForm(@PathVariable Long itemId) {
        ModelAndView mav = new ModelAndView(itemFormPath);
        mav.addObject("item", itemService.getItemById(itemId));
        return mav;
    }


    @PostMapping("/create")
    public String createItem(@Valid @ModelAttribute("item") ItemDTO itemDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "item-form";
        }

        itemDTO.setCreatedAt(java.time.LocalDateTime.now());
        itemDTO.setId(null);

        itemService.addItem(itemDTO);

        redirectAttributes.addFlashAttribute("message", "Artikel '" + itemDTO.getName() + "' erfolgreich erstellt!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/admin";
    }

    @PostMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        try {
            this.itemService.deleteItemById(itemId);
            return "redirect:/admin";
        } catch (NoSuchElementException nse) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    

    // @PostMapping("/edit/submit/{itemId}")
    // public String updateForm(@Valid @ModelAttribute("item") ItemDTO itemDTO,
    //                          BindingResult bindingResult,
    //                          RedirectAttributes redirectAttributes) {

    //     if (bindingResult.hasErrors()) {
    //         return "item-form";
    //     }

    //     itemDTO.setCreatedAt(java.time.LocalDateTime.now());

    //     itemService.updateItem(itemDTO);

    //     redirectAttributes.addFlashAttribute("message", "Artikel '" + itemDTO.getName() + "' erfolgreich bearbeitet!");
    //     redirectAttributes.addFlashAttribute("messageType", "success");

    //     return "redirect:/admin";
    // }
    
}
