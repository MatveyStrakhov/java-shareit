package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.CommentCreationWithoutBookingException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> returnAllItems(Long userId) {
        List<ItemDto> items = itemRepository.findByOwnerId(userId);
        items.replaceAll((ItemDto itemDto) -> {
            if (itemDto.getOwnerId() == userId) {
                return getItemById(itemDto.getId(), userId);
            } else return itemDto;
        });
        return items;
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IncorrectItemIdException("No such item!"));
        if (item.getOwner().getId() == userId) {
            List<BookingShortDto> listForLastBooking = bookingRepository.findLastByItemId(id, LocalDateTime.now());
            List<BookingShortDto> listForNextBooking = bookingRepository.findNextByItemId(id, LocalDateTime.now());
            BookingShortDto lastBooking;
            BookingShortDto nextBooking;
            if (listForNextBooking.isEmpty()) {
                nextBooking = null;
            } else {
                nextBooking = listForNextBooking.get(0);
            }
            if (listForLastBooking.isEmpty()) {
                lastBooking = null;
            } else {
                lastBooking = listForLastBooking.get(0);
            }
            return ItemMapper.toItemDtoWithBookings(item, lastBooking, nextBooking, commentRepository.findCommentsByItemId(id));

        } else {
            return ItemMapper.toItemDto(item, commentRepository.findCommentsByItemId(id));
        }
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
            return ItemMapper.toItemDto(itemRepository.save(newItem), commentRepository.findCommentsByItemId(itemId));

        } else {
            throw new IncorrectItemIdException("Not your item!");
        }
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        Item newItem = ItemMapper.toItem(item, user);
        Item savedItem = itemRepository.save(newItem);

        return ItemMapper.toItemDto(savedItem, commentRepository.findCommentsByItemId(savedItem.getId()));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemRepository.findByNameAndDescription(text.toLowerCase());
        }
    }

    @Override
    public CommentDto createComment(CommentCreateDto commentCreateDto, Long itemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        if (bookingRepository.isBooker(userId, itemId, LocalDateTime.now())) {
            Comment comment = CommentMapper.toComment(commentCreateDto, userId, itemId);
            return CommentMapper.toCommentDto(commentRepository.save(comment), user);
        } else throw new CommentCreationWithoutBookingException("You haven't booked this item!");
    }
}
