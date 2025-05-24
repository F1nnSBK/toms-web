package com.toms.app.service;

import com.toms.app.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAllItems();
    ItemDTO getItemById(Long id);
    ItemDTO updateItem(ItemDTO item);
    void deleteItemById(Long id);
    ItemDTO addItem(ItemDTO item);
}
