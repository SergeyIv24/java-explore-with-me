package ru.practicum.events;

import lombok.Getter;

@Getter
public enum EventStates {
    WAITING(1),
    PUBLISHED(2),
    CANCELED(3);

    private final int state;

    EventStates(int state) {
        this.state = state;
    }
}
