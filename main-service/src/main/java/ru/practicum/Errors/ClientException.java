package ru.practicum.Errors;

public class ClientException extends RuntimeException {

    public ClientException(final String message) {
        super(message);
    }
}
