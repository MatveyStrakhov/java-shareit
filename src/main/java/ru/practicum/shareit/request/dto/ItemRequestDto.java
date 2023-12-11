package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Data
public class ItemRequestDto {
    long id;
    String description;
    long requestorId;
    LocalDateTime created;
}
