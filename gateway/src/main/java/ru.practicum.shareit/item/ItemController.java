package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping()
    public ResponseEntity<Object> getAllItems(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                              @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemClient.getItemById(id, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto item, @PathVariable("id") Long itemId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.updateItem(itemId, userId, item);
    }

    @PostMapping()
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createItem(userId, item);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text, @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                             @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentCreateDto commentCreateDto, @PathVariable("id") Long itemId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createComment(userId, itemId, commentCreateDto);
    }
}
