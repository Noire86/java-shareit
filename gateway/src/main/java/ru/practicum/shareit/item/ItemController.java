package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> addNewItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated @RequestBody ItemCreationDto itemDto) {

        log.info("Creating new item: userId={} item={}", userId, itemDto);
        return client.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> amendItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer itemId,
            @Validated @RequestBody ItemDto itemDto) {

        log.info("Updating itemId={} by userId={} newItem={}", itemId, userId, itemDto);
        return client.amendItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer itemId) {

        log.info("Getting itemId={} for userId={}", itemId, userId);
        return client.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Getting all items by ownerId={}", userId);
        return client.getAllItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "text") String text,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        if (text.isEmpty()) return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

        log.info("Searching item by keyword '{}'", text);
        return client.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewComment(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer itemId,
            @RequestBody @Validated CommentCreationDto commentCreationDto) {

        log.info("Creating new comment for itemId={}, by userId={} with comment={}", itemId, userId, commentCreationDto);
        return client.addNewComment(userId, itemId, commentCreationDto);
    }
}