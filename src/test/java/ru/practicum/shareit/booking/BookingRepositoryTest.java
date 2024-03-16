package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    BookingRepository bookingRepository;
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
            .build();
    private final Booking booking = Booking.builder()
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .booker(user)
            .item(item)
            .build();
    private final Booking pastBooking = Booking.builder()
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().minusHours(100L))
            .end(LocalDateTime.now().minusHours(90L))
            .booker(user)
            .item(item)
            .build();
    private final Booking futureBooking = Booking.builder()
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().plusHours(100L))
            .end(LocalDateTime.now().plusHours(190L))
            .booker(user)
            .item(item)
            .build();

    @BeforeEach
    void beforeEach() {
        em.persist(user);
        em.persist(userOwner);
        em.persist(item);
        em.flush();
        bookingRepository.save(booking);
        bookingRepository.save(futureBooking);
        bookingRepository.save(pastBooking);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        em.clear();
    }

    @Order(1)
    @Test
    void testIsBooker() {
        assertTrue(bookingRepository.isBooker(1L, 1L, LocalDateTime.now()));
        assertFalse(bookingRepository.isBooker(2L, 1L, LocalDateTime.now()));
        assertFalse(bookingRepository.isBooker(1L, 2L, LocalDateTime.now()));
        assertFalse(bookingRepository.isBooker(1L, 1L, LocalDateTime.now().minusDays(20L)));
    }

    @Order(2)
    @Test
    void testFindByBookerId() {
        assertEquals(bookingRepository.findByBookerId(1L, PageRequest.of(0, 10)).get(0).getId(), 3L);
        assertEquals(bookingRepository.findByBookerId(2L, PageRequest.of(0, 10)).size(), 0);
    }

    @Order(3)
    @Test
    void testFindByBookerIdAndFutureState() {
        assertEquals(bookingRepository.findByBookerIdAndFutureState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 2L);
        assertEquals(bookingRepository.findByBookerIdAndFutureState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(4)
    @Test
    void testFindByBookerIdAndCurrentState() {
        assertEquals(bookingRepository.findByBookerIdAndCurrentState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 1L);
        assertEquals(bookingRepository.findByBookerIdAndCurrentState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(5)
    @Test
    void testFindByBookerIdAndPastState() {
        assertEquals(bookingRepository.findByBookerIdAndPastState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 3L);
        assertEquals(bookingRepository.findByBookerIdAndPastState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(6)
    @Test
    void testFindByBookerIdAndStatus() {
        assertEquals(bookingRepository.findByBookerIdAndStatus(1L, BookingStatus.WAITING, PageRequest.of(0, 10)).size(), 3);
        assertEquals(bookingRepository.findByBookerIdAndStatus(2L, BookingStatus.WAITING, PageRequest.of(0, 10)).size(), 0);
        assertEquals(bookingRepository.findByBookerIdAndStatus(1L, BookingStatus.APPROVED, PageRequest.of(0, 10)).size(), 0);
        assertEquals(bookingRepository.findByBookerIdAndStatus(1L, BookingStatus.REJECTED, PageRequest.of(0, 10)).size(), 0);
    }

    @Order(7)
    @Test
    void testFindByOwnerId() {
        assertEquals(bookingRepository.findByOwnerId(2L, PageRequest.of(0, 10)).get(0).getId(), 3L);
        assertEquals(bookingRepository.findByOwnerId(1L, PageRequest.of(0, 10)).size(), 0);
    }

    @Order(8)
    @Test
    void testFindByOwnerIdAndFutureState() {
        assertEquals(bookingRepository.findByOwnerIdAndFutureState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 2L);
        assertEquals(bookingRepository.findByOwnerIdAndFutureState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(9)
    @Test
    void testFindByOwnerIdAndCurrentState() {
        assertEquals(bookingRepository.findByOwnerIdAndCurrentState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 1L);
        assertEquals(bookingRepository.findByOwnerIdAndCurrentState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(10)
    @Test
    void testFindByOwnerIdAndPastState() {
        assertEquals(bookingRepository.findByOwnerIdAndPastState(2L, LocalDateTime.now(), PageRequest.of(0, 10)).get(0).getId(), 3L);
        assertEquals(bookingRepository.findByOwnerIdAndPastState(1L, LocalDateTime.now(), PageRequest.of(0, 10)).size(), 0);
    }

    @Order(11)
    @Test
    void testFindByOwnerIdAndStatus() {
        assertEquals(bookingRepository.findByOwnerIdAndStatus(2L, BookingStatus.WAITING, PageRequest.of(0, 10)).size(), 3);
        assertEquals(bookingRepository.findByOwnerIdAndStatus(1L, BookingStatus.WAITING, PageRequest.of(0, 10)).size(), 0);
        assertEquals(bookingRepository.findByOwnerIdAndStatus(2L, BookingStatus.APPROVED, PageRequest.of(0, 10)).size(), 0);
        assertEquals(bookingRepository.findByOwnerIdAndStatus(2L, BookingStatus.REJECTED, PageRequest.of(0, 10)).size(), 0);
    }

    @Order(12)
    @Test
    void testFindLastByItemId() {
        assertEquals(bookingRepository.findLastByItemId(1L, LocalDateTime.now()).get(0).getId(), 1L);
        assertEquals(bookingRepository.findLastByItemId(2L, LocalDateTime.now()).size(), 0);
    }

    @Order(13)
    @Test
    void testFindNextByItemId() {
        assertEquals(bookingRepository.findNextByItemId(1L, LocalDateTime.now()).get(0).getId(), 2L);
        assertEquals(bookingRepository.findNextByItemId(2L, LocalDateTime.now()).size(), 0);
    }
}
