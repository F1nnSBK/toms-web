package com.toms.app.mapper;

import com.toms.app.dto.ItemDTO;
import com.toms.app.model.Item;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO itemToItemDTO(Item item);
    List<ItemDTO> itemsToItemDTOs(List<Item> items);

    @InheritInverseConfiguration
    Item itemDTOToItem(ItemDTO itemDTO);
    List<Item> itemDTOsToItems(List<ItemDTO> itemDTOs);

}
