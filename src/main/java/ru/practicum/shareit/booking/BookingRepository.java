package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1 order by b.id DESC")
    List<BookingDto> findByBookerId(Long userId, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1 and b.start > ?2 order by b.id DESC")
    List<BookingDto> findByBookerIdAndFutureState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1 and b.end < ?2 order by b.id DESC")
    List<BookingDto> findByBookerIdAndPastState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1 and b.start < ?2 and b.end > ?2")
    List<BookingDto> findByBookerIdAndCurrentState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1 and b.status like ?2")
    List<BookingDto> findByBookerIdAndStatus(Long userId, BookingStatus status, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where i.owner.id = ?1 order by b.id DESC")
    List<BookingDto> findByOwnerId(Long userId, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where i.owner.id = ?1 and b.status like ?2")
    List<BookingDto> findByOwnerIdAndStatus(Long userId, BookingStatus status, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where i.owner.id = ?1 and b.start > ?2 order by b.id DESC")
    List<BookingDto> findByOwnerIdAndFutureState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where i.owner.id = ?1 and b.end < ?2 order by b.id DESC")
    List<BookingDto> findByOwnerIdAndPastState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where i.owner.id = ?1 and b.start < ?2 and b.end > ?2")
    List<BookingDto> findByOwnerIdAndCurrentState(Long userId, LocalDateTime currentTime, PageRequest pageRequest);

    @Query("select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.start, b.end, b.item.id, b.booker.id, b.status) from Booking b join b.item i join b.booker u where i.id = ?1 and b.start <= ?2 order by b.start DESC")
    List<BookingShortDto> findLastByItemId(Long itemId, LocalDateTime now);

    @Query("select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.start, b.end, b.item.id, b.booker.id, b.status) from Booking b join b.item i join b.booker u where i.id = ?1 and b.start > ?2 and b.status != ru.practicum.shareit.booking.BookingStatus.REJECTED order by b.start ASC")
    List<BookingShortDto> findNextByItemId(Long itemId, LocalDateTime now);

    @Query("select count(b)>0 from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.status != ru.practicum.shareit.booking.BookingStatus.REJECTED and b.start < ?3")
    boolean isBooker(Long userId, Long itemId, LocalDateTime now);
}
