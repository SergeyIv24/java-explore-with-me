package ru.practicum.errors;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
