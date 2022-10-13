package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

/**
 * // Ожидает реализации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestBody @Validated ItemRequestCreationDto creationDto) {

        return new ResponseEntity<>(service.createRequest(userId, creationDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestExtendedDto>> getPersonalItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId) {

        return new ResponseEntity<>(service.getPersonalItemRequests(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestExtendedDto>> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {

        return new ResponseEntity<>(service.getAllItemRequestsPaged(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestExtendedDto> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer requestId) {

        return new ResponseEntity<>(service.getItemRequestById(userId, requestId), HttpStatus.OK);
    }

}
