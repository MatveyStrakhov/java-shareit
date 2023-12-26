package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    @JsonIgnore
    long ownerId;
    @JsonIgnore
    Long requestId;
}
