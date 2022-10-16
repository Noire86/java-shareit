package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestBody @Validated ItemRequestCreationDto creationDto) {

        log.info("Creating new request for userId={} newRequest={}", userId, creationDto);
        return client.createRequest(userId, creationDto);
    }

    @GetMapping
    public ResponseEntity<Object> getPersonalItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId) {

        log.info("Getting all requests for userId={}", userId);
        return client.getPersonalItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Getting all item requests...");
        return client.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer requestId) {

        log.info("Getting request by id={}", requestId);
        return client.getItemRequestById(userId, requestId);
    }
}
