package ru.practicum.events.model;

import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventServicePublic {

    Collection<EventRespShort> searchEvents(String text, List<Integer> categories, boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                                            String sort, int from, int size);

    EventRespFull getEvent(long eventId);

}
