package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.ValidationException;

public class PaginationUtils {

    public static void verifyPaginationParams(Integer from, Integer size) {
        if (from == null || from < 0) {
            throw new ValidationException("Invalid starting pagination parameter!", HttpStatus.BAD_REQUEST);
        }

        if (size == null || size < 1) {
            throw new ValidationException("Invalid page amount pagination parameter!", HttpStatus.BAD_REQUEST);
        }
    }

    public static Pageable handlePaginationParams(Integer from, Integer size) {

        if (from == null && size == null) {
            return Pageable.unpaged();
        } else {
            PaginationUtils.verifyPaginationParams(from, size);
            return PageRequest.of(from / size, size);
        }
    }
}
