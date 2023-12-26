package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) from Booking b join b.item i join b.booker u where u.id = ?1")
    List<BookingDto> findByBookerId(Long userId);
}
