package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import java.util.List;


@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select new ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto(ir.id, ir.description, u.id, ir.created) from ItemRequest ir join ir.requestor u where u.id = ?1")
    List<ItemRequestWithResponsesDto> findByRequestorId(Long requestorId);
}
