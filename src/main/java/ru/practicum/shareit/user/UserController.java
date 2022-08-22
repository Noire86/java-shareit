package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * // TODO .
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getUser(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @Validated @RequestBody UserDto user, BindingResult errors) throws ValidationException {
        if (errors.hasErrors()) throw new ValidationException(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.updateUser(id, user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto user, BindingResult errors) throws ValidationException {
        if (errors.hasErrors()) throw new ValidationException(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

}
