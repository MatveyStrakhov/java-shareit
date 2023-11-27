package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 **/
@Data
@Builder
public class User {
    long id;
    @NotBlank
    String name;
    @NotNull
    @Email
    String email;
}
