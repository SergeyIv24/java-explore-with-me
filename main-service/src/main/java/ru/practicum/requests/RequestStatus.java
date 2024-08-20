package ru.practicum.requests;

import lombok.Getter;

@Getter
public enum RequestStatus {
    PENDING(1),
    ACCEPTED(2),
    CANCELED(3);

    private final int requestStatus;

    RequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }
}
