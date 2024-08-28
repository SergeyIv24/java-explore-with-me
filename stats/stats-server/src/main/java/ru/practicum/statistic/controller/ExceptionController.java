package ru.practicum.statistic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statistic.exceptions.ErrorMessage;
import ru.practicum.statistic.exceptions.NotFoundException;
import ru.practicum.statistic.exceptions.ValidationException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlerNotFoundException(final NotFoundException e) {
        return new ErrorMessage("Not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(final ValidationException e) {
        return new ErrorMessage("Validation exception", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ErrorMessage("Bad input data", e.getMessage());
    }


}
