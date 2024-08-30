package ru.practicum.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.common.GeneralConstants;

import java.time.LocalDateTime;

@RestControllerAdvice
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
    public ErrorMessage handleConflict(final ConflictException e) {
        String reason = "Integrity constraint has been violated";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.CONFLICT.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflict(final DataIntegrityViolationException e) {
        String reason = "Integrity constraint has been violated";
        String message = "could not execute statement; constraint " + e.getMostSpecificCause();
        return new ErrorMessage(HttpStatus.CONFLICT.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(final NotFoundException e) {
        String reason = "The required object was not found.";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.NOT_FOUND.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(final ValidationException e) {
        String reason = "Incorrectly made request.";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.BAD_REQUEST.getReasonPhrase(), reason, message, prepareResponseDate());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        String reason = "Incorrectly made request.";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.BAD_REQUEST.getReasonPhrase(), reason, message, prepareResponseDate());
    }

/*    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleThrowable(final Throwable e) {
        String reason = "Something went wrong";
        String message = e.getMessage();
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                reason, message, prepareResponseDate());
    }*/

    private String prepareResponseDate() {
        return LocalDateTime.now().format(GeneralConstants.DATE_FORMATTER);
    }
}
