package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public List<ItemDto> returnAllItems(Long userId) {
        return itemStorage.returnAllItems(userId);
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
        return itemStorage.updateItem(userId, itemId, item);
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        return itemStorage.createItem(userId, item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text);
    }
}
