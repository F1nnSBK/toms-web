package com.toms.app.controller;

import java.util.Comparator;
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
    private static final String ITEM_FORM = "item-form";
    private static final String ADMIN_REDIRECT = "redirect:/admin";
    
    public ItemManagerControllerImpl(ItemService itemService) {
        this.itemService = itemService;
    }
        
    @Override
    @GetMapping
    public ModelAndView adminItemManager() {
        List<ItemDTO> allItems = itemService.getAllItems().stream()
            .sorted(Comparator.comparing(ItemDTO::getCreatedAt).reversed())
            .toList();

        ModelAndView mav = new ModelAndView("itemManager");
        mav.addObject("items", allItems);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView showCreateForm(){
        ModelAndView mav = new ModelAndView(ITEM_FORM);
        mav.addObject("item", new ItemDTO());
        return mav;
    }

    @GetMapping("/edit/{itemId}")
    public ModelAndView showCreateForm(@PathVariable Long itemId) {
        ModelAndView mav = new ModelAndView(ITEM_FORM);
        mav.addObject("item", this.itemService.getItemById(itemId));
        return mav;
    }

    @PostMapping("/create")
    public String createItem(@Valid @ModelAttribute("item") ItemDTO itemDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return ITEM_FORM;
        }

        itemDTO.setCreatedAt(java.time.LocalDateTime.now());
        itemDTO.setId(null);

        itemService.addItem(itemDTO);

        redirectAttributes.addFlashAttribute("message", "Artikel '" + itemDTO.getName() + "' erfolgreich erstellt!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return ADMIN_REDIRECT;
    }

    @PostMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        try {
            this.itemService.deleteItemById(itemId);
            return ADMIN_REDIRECT;
        } catch (NoSuchElementException nse) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/edit/{itemId}")
    public String updateForm(@Valid @ModelAttribute("item") ItemDTO itemDTO,
                             @PathVariable Long itemId,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return ITEM_FORM;
        }

        itemDTO.setCreatedAt(java.time.LocalDateTime.now());
        itemDTO.setId(itemId);
        itemService.updateItem(itemDTO);

        redirectAttributes.addFlashAttribute("message", "Artikel '" + itemDTO.getName() + "' erfolgreich bearbeitet!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return ADMIN_REDIRECT;
    }
    
}
