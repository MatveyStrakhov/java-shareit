package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> returnAllItems(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IncorrectItemIdException("No such item!"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
        Item previous = itemRepository.findById(itemId).orElseThrow(() -> new IncorrectItemIdException("No such item!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        item.setId(itemId);
        if (previous.getOwner().getId() == userId) {
            if (item.getName() == null) {
                item.setName(previous.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(previous.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(previous.getAvailable());
            }
            if (item.getRequestId() == null) {
                item.setRequestId(previous.getRequestId());
            }
            item.setOwnerId(userId);

            Item newItem = ItemMapper.toItem(item, user);
            return ItemMapper.toItemDto(itemRepository.save(newItem));

        } else {
            throw new IncorrectItemIdException("Not your item!");
        }
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        Item newItem = ItemMapper.toItem(item, user);

        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemRepository.findByNameAndDescription(text.toLowerCase());
        }
    }
}
