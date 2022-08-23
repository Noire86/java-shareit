package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * // TODO .
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> addNewItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.addItem(userId, itemDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> amendItem(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer itemId,
            @Validated @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.amendItem(userId, itemId, itemDto), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Integer itemId) {
        return new ResponseEntity<>(itemService.getItem(itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return new ResponseEntity<>(itemService.getAllItemsByOwner(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItem(@RequestParam(name = "text") String text) {
        return new ResponseEntity<>(itemService.search(text), HttpStatus.OK);
    }
}
