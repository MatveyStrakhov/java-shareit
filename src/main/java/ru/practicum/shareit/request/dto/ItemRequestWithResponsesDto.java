package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestWithResponsesDto {
    private long id;
    private String description;
    private long requestorId;
    private LocalDateTime created;
    private List<ItemResponseDto> items;

    public ItemRequestWithResponsesDto(long id, String description, long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
        this.items = Collections.emptyList();
    }
}
