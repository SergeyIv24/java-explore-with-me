package ru.practicum.Errors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.GeneralConstants;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "ru.practicum")
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String reason = e.getBody().getDetail();
        String message = "Field: " + e.getBindingResult().getFieldError().getField() +
                " error: " + e.getBindingResult().getFieldError().getDefaultMessage();
        return new ErrorMessage(HttpStatus.BAD_REQUEST.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflict(ConstraintViolationException e) {
        String reason = "Integrity constraint has been violated";
        String message = "could not execute statement; constraint" + e.getConstraintName();
        return new ErrorMessage(HttpStatus.CONFLICT.getReasonPhrase(), reason, message, prepareResponseDate());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException e) {
        String reason = "The required object was not found.";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.NOT_FOUND.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    private String prepareResponseDate() {
        return LocalDateTime.now().format(GeneralConstants.DATE_FORMATTER);
    }
}
