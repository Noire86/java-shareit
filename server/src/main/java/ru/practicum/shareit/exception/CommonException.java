package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Абстрактное базовое исключение для приложения
 */
@Getter
public abstract class CommonException extends RuntimeException {

    private final String message;
    private final HttpStatus code;

    protected CommonException(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }
}