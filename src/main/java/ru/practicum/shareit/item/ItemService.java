package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> returnAllItems(Long userId);

    ItemDto getItemById(Long id);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    ItemDto createItem(Long userId, Item item);

    Collection<ItemDto> searchItems(String text);
}
