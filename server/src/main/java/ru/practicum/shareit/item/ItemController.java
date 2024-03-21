package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> getAllItems(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                     @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return itemService.returnAllItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemService.getItemById(id, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto item, @PathVariable("id") Long itemId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(userId, itemId, item);
    }

    @PostMapping()
    public ItemDto addItem(@Valid @RequestBody ItemDto item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createItem(userId, item);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text, @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                    @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentCreateDto commentCreateDto, @PathVariable("id") Long itemId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createComment(commentCreateDto, itemId, userId);
    }

}
