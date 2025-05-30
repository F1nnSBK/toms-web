package com.toms.app.service;

import com.toms.app.dto.ItemDTO;
import com.toms.app.mapper.ItemMapper;
import com.toms.app.model.Item;
import com.toms.app.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    ItemMapper itemMapper;

    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    public List<ItemDTO> getAllItems() {
        if(this.itemRepository.findAll().isEmpty()) {
            log.info("No items available");
        }
        return this.itemMapper.itemsToItemDTOs(this.itemRepository.findAll());
    }

    public List<ItemDTO> getFeaturedItems() {
        if(this.itemRepository.findAll().isEmpty()) {
            log.info("No items available");
        }
        List<ItemDTO> featuredItems = this.itemMapper.itemsToItemDTOs(this.itemRepository.findAll());
        Collections.shuffle(featuredItems);
        return featuredItems.subList(0, 4);
    }

    public ItemDTO getItemById(Long id) {
        try{
            return this.itemMapper.itemToItemDTO(this.itemRepository.findById(id).get());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Item with id " + id +" not found", e);
        }
    }


    public ItemDTO updateItem(ItemDTO item) {
        if(itemRepository.existsById(item.getId())) {
            Item tmp = this.itemMapper.itemDTOToItem(item);
            return this.itemMapper.itemToItemDTO(this.itemRepository.save(tmp));
        } else {
            throw new EntityNotFoundException("Item to be updated not found. Item Id: " + item.getId());
        }
    }


    public void deleteItemById(Long id) {
        this.itemRepository.deleteById(id);
    }


    public ItemDTO addItem(ItemDTO item) {
        try{
            Item tmp = this.itemMapper.itemDTOToItem(item);
            tmp.setId(null);
            log.info("Creating new item: {}", item.getName());
            return this.itemMapper.itemToItemDTO(this.itemRepository.save(tmp));
        } catch (Exception e) {
            throw new RuntimeException("Error adding Item " + item.getName(), e);
        }
    }
}
