package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemRequestWithResponsesDto> returnAllItemRequests() {
        List<ItemRequest> listOfItemRequest = itemRequestRepository.findAll();
        return listOfItemRequest.stream()
                .map((itemRequest -> ItemRequestMapper.toItemRequestWithResponsesDto(itemRequest, itemRepository.findByRequestId(itemRequest.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithResponsesDto> returnAllItemRequestsByUserId(long userId) {
        if (userRepository.existsById(userId)) {
            List<ItemRequestWithResponsesDto> itemRequestDtoList = itemRequestRepository.findByRequestorId(userId);
            if (itemRequestDtoList != null && !itemRequestDtoList.isEmpty()) {
                itemRequestDtoList.forEach((irl) -> irl.setItems(itemRepository.findByRequestId(irl.getId())));
                return itemRequestDtoList;
            } else return Collections.emptyList();
        } else throw new NotFoundUserException("User not found!");
    }

    @Override
    public ItemRequestDto createItemRequest(ItemRequestCreateDto createDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(createDto, user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithResponsesDto> returnItemRequestsByPage(int from, int size, long userId) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> page = itemRequestRepository.findAll(pageRequest);
        List<ItemRequest> listOfItemRequest;
        if (page.hasContent()) {
            listOfItemRequest = page.getContent();
            return listOfItemRequest.stream().filter((itemRequest -> itemRequest.getRequestor().getId() != userId))
                    .map((itemRequest -> ItemRequestMapper.toItemRequestWithResponsesDto(itemRequest, itemRepository.findByRequestId(itemRequest.getId()))))
                    .collect(Collectors.toList());
        } else return Collections.emptyList();
    }

    @Override
    public ItemRequestWithResponsesDto returnItemRequestDtoById(long userId, long itemRequestId) {
        if (userRepository.existsById(userId)) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new ItemRequestNotFoundException("Item request not found!"));
            return ItemRequestMapper.toItemRequestWithResponsesDto(itemRequest, itemRepository.findByRequestId(itemRequestId));

        } else throw new NotFoundUserException("User not found!");
    }
}
