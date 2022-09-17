package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.model.ExceptionResponse;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Эдвайс для обработки исключений и их вывода в response
 * Добавлен новый хендлер исключения
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @ExceptionHandler({
            ValidationException.class,
            StorageException.class,
            AccessViolationException.class
    })
    public ResponseEntity<ExceptionResponse> handleCommonException(CommonException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .error(ex.getMessage())
                .exceptionType(ex.getClass().getSimpleName())
                .timestamp(String.format("[%s]", LocalDateTime.now().format(formatter)))
                .statusCode(String.format("%s: %s", ex.getCode().value(), ex.getCode().getReasonPhrase()))
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, ex.getCode());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .error(ex.getMessage())
                .exceptionType(ex.getClass().getSimpleName())
                .timestamp(String.format("[%s]", LocalDateTime.now().format(formatter)))
                .statusCode(String.format("%s: %s", HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()))
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}