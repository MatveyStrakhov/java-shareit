package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    UserRepository userRepository;
    private final User user = User.builder()
            .name("username")
            .email("email@gmail.com")
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@gmail.com")
            .build();

    @BeforeEach
    void beforeEach() {
        em.persist(user);
        userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        em.clear();
    }

    @Order(1)
    @Test
    void testFindAllAndReturnDto() {
        assertEquals(userRepository.findAllAndReturnDto(), List.of(userDto));

    }

}
