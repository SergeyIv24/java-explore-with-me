package ru.practicum.events;

import ru.practicum.events.dto.EventFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;

import java.util.Collection;

public interface EventService {

    EventRequest createEvent(EventRequest eventRequest, long userId);

    Collection<EventRespShort> getUsersEvents(long userId);

    EventFull getUsersFullEvent(long userId, long eventId);

    EventRequest updateUsersEvent(long userId, long eventId, EventRequest eventRequest);

    EventRequest adminsUpdate(EventRequest eventRequest, long eventId);
}
