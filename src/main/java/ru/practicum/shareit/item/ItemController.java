package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public Collection<ItemDto> getAllItems(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.returnAllItems(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody Item item, @PathVariable("id") Long itemId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(userId, itemId, item);
    }

    @PostMapping()
    public ItemDto addItem(@Valid @RequestBody Item item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createItem(userId, item);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}
