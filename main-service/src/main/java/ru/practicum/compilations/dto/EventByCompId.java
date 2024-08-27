package ru.practicum.compilations.dto;

import ru.practicum.events.model.Event;

public interface EventByCompId {

    Integer getCompilationId();

    Event getEvent();

}
