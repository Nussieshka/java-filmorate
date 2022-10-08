package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRuntimeException(Exception exception) {
        log.error(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { ValidationException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception exception) {
        log.error((exception instanceof ValidationException ? "ValidationException " : "MethodArgumentNotValidException ")
                + "was thrown");
        log.error(exception.getMessage());
    }
}
