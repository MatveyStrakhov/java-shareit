package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDtoWithBookings(Item item, BookingShortDto lastBooking, BookingShortDto nextBooking, List<CommentDto> comments) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .requestId(item.getRequestId())
                .comments(comments)
                .build();
        return itemDto;
    }

    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .requestId(item.getRequestId())
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
