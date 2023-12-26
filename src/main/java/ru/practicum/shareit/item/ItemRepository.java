package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select new ru.practicum.shareit.item.dto.ItemDto(i.id, i.name, i.description, i.available, u.id, i.requestId) from Item i join i.owner u where u.id = ?1")
    List<ItemDto> findByOwnerId(long id);

    @Query("select new ru.practicum.shareit.item.dto.ItemDto(i.id, i.name, i.description, i.available, u.id, i.requestId) " +
            "from Item i join i.owner u where i.available = true and lower(i.name) || lower(i.description) like %:text% ")
    List<ItemDto> findByNameAndDescription(@Param("text") String text);
}
