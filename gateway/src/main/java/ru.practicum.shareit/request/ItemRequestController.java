package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequestsWithResponses(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.returnAllItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PositiveOrZero @RequestParam(required = false, value = "from") Integer from,
                                              @Valid @Positive @RequestParam(required = false, value = "size") Integer size) {
        if (from != null && size != null) {
            return itemRequestClient.getItemRequests(userId, from, size);
        } else {
            return itemRequestClient.getAllItemRequests(userId);
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getItemRequestById(requestId,userId);
    }
}
