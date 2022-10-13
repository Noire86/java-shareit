package ru.practicum.shareit.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.model.CommonException;
import ru.practicum.shareit.exception.model.ExceptionResponse;
import ru.practicum.shareit.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Эдвайс для обработки исключений и их вывода в response
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleCommonException(CommonException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .error(ex.getMessage())
                .exceptionType(ex.getClass().getSimpleName())
                .timestamp(String.format("[%s]", LocalDateTime.now().format(formatter)))
                .statusCode(String.format("%s: %s", ex.getCode().value(), ex.getCode().getReasonPhrase()))
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, ex.getCode());
    }
}