package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

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

    public static ItemRequest toItemRequest(ItemRequestCreateDto createDto, User user) {
        ItemRequest itemRequest = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description(createDto.getDescription())
                .requestor(user)
                .build();
        return itemRequest;
    }

    public static ItemRequestWithResponsesDto toItemRequestWithResponsesDto(ItemRequest itemRequest, List<ItemResponseDto> responseDtoList) {
        ItemRequestWithResponsesDto itemRequestWithResponsesDto = ItemRequestWithResponsesDto.builder()
                .items(responseDtoList)
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .id(itemRequest.getId())
                .build();
        return itemRequestWithResponsesDto;
    }
}
