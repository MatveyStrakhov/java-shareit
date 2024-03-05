package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> returnAllItems(Long userId);

    ItemDto getItemById(Long id,Long userId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto item);

    ItemDto createItem(Long userId, ItemDto item);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentCreateDto commentCreateDto, Long itemId, Long userId);
}
