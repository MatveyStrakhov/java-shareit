package ru.practicum.shareit.item.model;


import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @Column(name = "is_available")
    @NotNull
    Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @Column(name = "request_id")
    Long requestId;
}

