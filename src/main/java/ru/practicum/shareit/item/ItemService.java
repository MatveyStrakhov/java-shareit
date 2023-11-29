package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> returnAllItems(Long userId);

    ItemDto getItemById(Long id);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    ItemDto createItem(Long userId, ItemDto item);

    List<ItemDto> searchItems(String text);
}
