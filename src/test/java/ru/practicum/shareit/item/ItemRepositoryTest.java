package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    ItemRepository itemRepository;
    private final User user = User.builder()
            .name("user")
            .email("email@gmail.com")
            .build();
    private final User userOwner = User.builder()
            .name("owner")
            .email("owner@gmail.com")
            .build();

    private final Item item = Item.builder()
            .name("item")
            .description("description")
            .owner(userOwner)
            .available(true)
            .requestId(1L)
            .build();
    private final ItemResponseDto itemResponseDto = ItemResponseDto.builder()
            .id(1L)
            .requestId(1L)
            .name("item")
            .description("description")
            .available(true)
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .description("need item")
            .created(LocalDateTime.now().plusHours(10))
            .requestor(user)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .requestId(1L)
            .ownerId(2L)
            .comments(null)
            .lastBooking(null)
            .nextBooking(null)
            .build();

    @BeforeEach
    void beforeEach() {
        em.persist(user);
        em.persist(userOwner);
        em.persist(itemRequest);
        em.persist(item);
        em.flush();
        itemRepository.save(item);
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        em.clear();
    }

    @Order(1)
    @Test
    void testFindByRequestId() {
        assertEquals(itemRepository.findByRequestId(1L), List.of(itemResponseDto));
        assertEquals(itemRepository.findByRequestId(2L), Collections.emptyList());
    }

    @Order(2)
    @Test
    void testFindByOwnerId() {
        assertEquals(itemRepository.findByOwnerId(2L, PageRequest.of(0, 20)).getContent(), List.of(itemDto));
        assertNotEquals(itemRepository.findByOwnerId(1L, PageRequest.of(0, 20)), Page.empty());
    }

    @Order(3)
    @Test
    void testFindByNameAndDescription() {
        assertEquals(itemRepository.findByNameAndDescription("description", PageRequest.of(0, 20)).getContent(), List.of(itemDto));
        assertNotEquals(itemRepository.findByNameAndDescription("madness", PageRequest.of(0, 20)), Page.empty());
    }
}
