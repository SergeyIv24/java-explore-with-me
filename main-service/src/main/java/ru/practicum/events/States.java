package ru.practicum.events;

import lombok.Getter;

@Getter
public enum States {
    WAITING(1),
    PUBLISHED(2),
    CANCELED(3);

    private final int state;

    States(int state) {
        this.state = state;
    }

}
