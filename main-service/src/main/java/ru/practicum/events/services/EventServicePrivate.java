package ru.practicum.events.services;

import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.dto.EventUpdate;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestResponse;
import ru.practicum.requests.dto.RequestsForConfirmation;

import java.util.Collection;

public interface EventServicePrivate {

    EventRequest createEvent(EventRequest eventRequest, long userId);

    Collection<EventRespShort> getUsersEvents(long userId, int from, int size);

    EventRespFull getUsersFullEventById(long userId, long eventId, String path);

    EventRequest updateUsersEvent(long userId, long eventId, EventUpdate eventUpdate);

    Collection<RequestDto> getRequestsByEventId(long eventId, long userId);

    RequestResponse approveRequests(RequestsForConfirmation requestsForConfirmation,
                                    long userId,
                                    long eventId);
}
