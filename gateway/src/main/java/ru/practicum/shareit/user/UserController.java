package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Getting all users...");
        return client.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Integer id) {
        log.info("Getting user with id {}", id);
        return client.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Integer id,
            @Validated @RequestBody UserDto user) {

        log.info("Updating userId={} newData={}", id, user);
        return client.updateUser(id, user);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated @RequestBody UserCreationDto user) {

        log.info("Creating new user: data={}", user);
        return client.addUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id) {

        log.info("Removing user: userId={}", id);
        return client.deleteUser(id);
    }
}
