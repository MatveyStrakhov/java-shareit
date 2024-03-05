package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .id(itemRequest.getId())
                .build();
        return itemRequestDto;
    }
}
