package ru.practicum.Errors;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
