package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequest {
    long id;
    String description;
    long requestorId;
    LocalDateTime created;
}
