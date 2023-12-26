package ru.practicum.shareit.item.dto;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Service
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .requestId(itemDto.getRequestId())
                .build();
        return item;
    }
}
