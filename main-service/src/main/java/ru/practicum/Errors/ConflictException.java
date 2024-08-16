package ru.practicum.Errors;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
