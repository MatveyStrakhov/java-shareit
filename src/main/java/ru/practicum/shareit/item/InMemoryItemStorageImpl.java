package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
//import ru.practicum.shareit.exception.IncorrectItemIdException;
//import ru.practicum.shareit.exception.IncorrectUserIdException;
//import ru.practicum.shareit.exception.NotFoundUserException;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemMapper;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.UserService;
//
//import java.util.*;
//import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class InMemoryItemStorageImpl {
//    private final UserService userService;
//    private Long itemId = 1L;
//    Map<Long, Item> items = new HashMap<>();
//
//    @Override
//    public List<ItemDto> returnAllItems(Long userId) {
//        return items.values().stream().filter(item -> item.getOwnerId() == userId).map(ItemMapper::toItemDto).collect(Collectors.toList());
//    }
//
//    @Override
//    public ItemDto getItemById(Long id) {
//        return ItemMapper.toItemDto(items.get(id));
//    }
//
//    @Override
//    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
//        if (userService.isValidUser(userId)) {
//            Item previous = items.get(itemId);
//            item.setId(itemId);
//            if (previous.getOwnerId() == userId) {
//                if (item.getName() == null) {
//                    item.setName(previous.getName());
//                }
//                if (item.getDescription() == null) {
//                    item.setDescription(previous.getDescription());
//                }
//                if (item.getAvailable() == null) {
//                    item.setAvailable(previous.getAvailable());
//                }
//                if (item.getRequestId() == null) {
//                    item.setRequestId(previous.getRequestId());
//                }
//                item.setOwnerId(userId);
//                items.put(item.getId(), ItemMapper.toItem(item));
//                return item;
//            } else {
//                throw new IncorrectItemIdException("Not your item!");
//            }
//        } else {
//            throw new IncorrectUserIdException("Not valid user!");
//        }
//    }
//
//    @Override
//    public ItemDto createItem(Long userId, ItemDto item) {
//        if (userService.isValidUser(userId)) {
//            item.setId(getId());
//            item.setOwnerId(userId);
//            items.put(item.getId(), ItemMapper.toItem(item));
//            return item;
//        } else throw new NotFoundUserException("Not valid user!");
//    }
//
//    @Override
//    public List<ItemDto> searchItems(String text) {
//        if (text.isEmpty()) {
//            return Collections.EMPTY_LIST;
//        } else {
//            return items.values().stream()
//                    .filter(item -> item.getAvailable().equals(true))
//                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) || item.getName().toLowerCase().contains(text.toLowerCase()))
//                    .map(ItemMapper::toItemDto)
//                    .collect(Collectors.toList());
//        }
//    }
//
//    private Long getId() {
//        return itemId++;
//    }

}
