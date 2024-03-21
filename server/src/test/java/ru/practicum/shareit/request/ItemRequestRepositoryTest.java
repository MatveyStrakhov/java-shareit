package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    private final User user = User.builder()
            .name("username")
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
    private final ItemRequest itemRequest = ItemRequest.builder()
            .requestor(user)
            .created(LocalDateTime.now())
            .description("need item")
            .build();
    private final ItemRequestWithResponsesDto itemRequestWithResponsesDto = ItemRequestWithResponsesDto.builder()
            .id(1L)
            .description("need item")
            .created(itemRequest.getCreated())
            .requestorId(user.getId())
            .items(Collections.emptyList())
            .build();

    @BeforeEach
    void beforeEach() {
        em.persist(user);
        em.persist(userOwner);
        em.persist(itemRequest);
        em.persist(item);
        em.flush();
        itemRequestRepository.save(itemRequest);
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        em.clear();
    }

    @Test
    void testFindByRequestorId() {
        assertEquals(itemRequestRepository.findByRequestorId(1L).size(), 1);
        assertEquals(itemRequestRepository.findByRequestorId(3L).size(), 0);
    }
}
