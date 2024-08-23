package ru.practicum.events.services;

import lombok.Getter;

@Getter
public enum EventStateAction {
    PUBLISH_EVENT(1),
    REJECT_EVENT(2),
    SEND_TO_REVIEW(3);


    private final int stateAction;

    EventStateAction(int stateAction) {
        this.stateAction = stateAction;
    }
}
