package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> returnAllBookings(Long userId) {
        return bookingRepository.findByBookerId(userId);
    }

    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        Booking newBooking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        if (newBooking.getBooker().getId() == userId || newBooking.getItem().getOwner().getId() == userId) {
            return BookingMapper.toBookingDto(newBooking);
        } else throw new IncorrectUserIdException("Not your booking!");
    }

    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, BookingUpdateDto booking) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        Booking newBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        if (user.getId() == newBooking.getBooker().getId() || user.getId() == newBooking.getItem().getOwner().getId()) {
            if (newBooking.getBooker().getId() != userId) {
                throw new IncorrectUserIdException("Not your booking!");
            }
            if (booking.getStart() != null && newBooking.getEnd().isAfter(booking.getStart())) {
                newBooking.setStart(booking.getStart());
            } else throw new StartAfterEndException("End before start!");
            if (booking.getEnd() != null && newBooking.getStart().isBefore(booking.getStart())) {
                newBooking.setStart(booking.getStart());
            } else throw new StartAfterEndException("End before start!");
            if (booking.getItemId() != null) {
                Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new IncorrectItemIdException("Item not found!"));
                newBooking.setItem(item);
            }

            return BookingMapper.toBookingDto(bookingRepository.save(newBooking));
        } else throw new IncorrectUserIdException("Not owner or booker!");
    }

    @Override
    public BookingDto createBooking(Long userId, BookingCreateDto booking) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new IncorrectItemIdException("Item not found!"));
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            throw new StartAfterEndException("Start is after or equals end");
        }
        if (item.getAvailable().equals(false)) {
            throw new ItemUnavailableException("Item is unavailable!");
        }
        Booking newBooking = BookingMapper.toBooking(booking, user, item);
        Booking savedBooking = bookingRepository.save(newBooking);
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto changeBookingStatus(Long bookingId, Long userId, Boolean approve) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("User not found!"));
        Booking newBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
        if (newBooking.getItem().getOwner().getId() != userId) {
            throw new IncorrectUserIdException("Not your booking!");
        }
        if (approve) {
            newBooking.setStatus(BookingStatus.APPROVED);
        } else {
            newBooking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(newBooking));
    }
}
