package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestWithResponsesDto> returnAllItemRequests();

    List<ItemRequestWithResponsesDto> returnAllItemRequestsByUserId(long userId);

    ItemRequestDto createItemRequest(ItemRequestCreateDto createDto, Long userId);

    List<ItemRequestWithResponsesDto> returnItemRequestsByPage(int from, int size,  long userId);

    ItemRequestWithResponsesDto returnItemRequestDtoById(long userId, long itemRequestId);
}
