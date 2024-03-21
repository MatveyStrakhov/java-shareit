package ru.practicum.shareit.user;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select new ru.practicum.shareit.user.dto.UserDto(u.id, u.name, u.email) from User u")
    List<UserDto> findAllAndReturnDto();
}
