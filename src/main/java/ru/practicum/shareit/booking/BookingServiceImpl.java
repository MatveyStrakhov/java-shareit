package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> returnAllBookings(Long userId, String state, int from, int size) {
        if (userRepository.existsById(userId)) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            switch (state) {
                case "ALL":
                    return bookingRepository.findByBookerId(userId, pageRequest).getContent();
                case "FUTURE":
                    return bookingRepository.findByBookerIdAndFutureState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "CURRENT":
                    return bookingRepository.findByBookerIdAndCurrentState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "PAST":
                    return bookingRepository.findByBookerIdAndPastState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "WAITING":
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageRequest).getContent();
                case "REJECTED":
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageRequest).getContent();
                default:
                    throw new UnsupportedBookingStateException("Unknown state: " + state);
            }

        } else throw new NotFoundUserException("Incorrect user id!");
    }

    @Override
    public List<BookingDto> returnAllBookingsByOwner(Long userId, String state, int from, int size) {
        if (userRepository.existsById(userId)) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            switch (state) {
                case "ALL":
                    return bookingRepository.findByOwnerId(userId, pageRequest).getContent();
                case "FUTURE":
                    return bookingRepository.findByOwnerIdAndFutureState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "CURRENT":
                    return bookingRepository.findByOwnerIdAndCurrentState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "PAST":
                    return bookingRepository.findByOwnerIdAndPastState(userId, LocalDateTime.now(), pageRequest).getContent();
                case "WAITING":
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING, pageRequest).getContent();
                case "REJECTED":
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageRequest).getContent();
                default:
                    throw new UnsupportedBookingStateException("Unknown state: " + state);
            }
        } else throw new NotFoundUserException("Incorrect user id!");
    }


    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        if (userRepository.existsById(userId)) {
            Booking newBooking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found!"));
            if (newBooking.getBooker().getId() == userId || newBooking.getItem().getOwner().getId() == userId) {
                return BookingMapper.toBookingDto(newBooking);
            } else throw new NotFoundUserException("Not your booking!");
        } else throw new NotFoundUserException("User not found!");
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
            if (booking.getEnd() != null && newBooking.getStart().isBefore(booking.getEnd())) {
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
        if (item.getOwner().getId() == userId) {
            throw new IncorrectItemIdException("Your Item!");
        }
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
            throw new NotFoundUserException("Not your booking!");
        }
        if (!newBooking.getStatus().equals(BookingStatus.APPROVED)) {
            if (approve) {
                newBooking.setStatus(BookingStatus.APPROVED);
            } else {
                newBooking.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.toBookingDto(bookingRepository.save(newBooking));
        } else throw new BookingDoubleApproveException("Booking already approved!");
    }
}
