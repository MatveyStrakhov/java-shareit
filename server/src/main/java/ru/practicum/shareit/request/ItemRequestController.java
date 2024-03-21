package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.createItemRequest(itemRequestCreateDto, userId);
    }

    @GetMapping
    public List<ItemRequestWithResponsesDto> getOwnRequestsWithResponses(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.returnAllItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithResponsesDto> getRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PositiveOrZero @RequestParam(required = false, value = "from") Integer from,
                                                         @Valid @Positive @RequestParam(required = false, value = "size") Integer size) {
        if (from != null && size != null) {
            return itemRequestService.returnItemRequestsByPage(from, size, userId);
        } else {
            return itemRequestService.returnAllItemRequests();
        }
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponsesDto getRequestById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("requestId") Long requestId) {
        return itemRequestService.returnItemRequestDtoById(userId, requestId);
    }
}
