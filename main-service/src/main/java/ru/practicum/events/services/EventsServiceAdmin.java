package ru.practicum.events.services;

import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespFull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventsServiceAdmin {

    EventRequest adminsUpdate(EventRequest eventRequest, long eventId);

    Collection<EventRespFull> getEventsByConditionalsForAdmin(List<Long> users,
                                                              List<String> states,
                                                              List<Integer> categories,
                                                              LocalDateTime rangeStart,
                                                              LocalDateTime rangeEnd,
                                                              int from,
                                                              int size);

}
