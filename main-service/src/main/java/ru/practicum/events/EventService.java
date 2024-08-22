package ru.practicum.events;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestsForConfirmation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {

    EventRequest createEvent(EventRequest eventRequest, long userId);

    Collection<EventRespShort> getUsersEvents(long userId, int from, int size);

    EventRespFull getUsersFullEventById(long userId, long eventId);

    EventRequest updateUsersEvent(long userId, long eventId, EventRequest eventRequest);

    EventRequest adminsUpdate(EventRequest eventRequest, long eventId);

    Collection<RequestDto> getRequestsByEventId(long eventId, long userId);

    Collection<RequestDto> approveRequests(RequestsForConfirmation requestsForConfirmation,
                                           long userId,
                                           long eventId);

    Collection<EventRespFull> getEventsByConditionalsForAdmin(List<Long> users,
                                                              List<String> states,
                                                              List<Integer> categories,
                                                              LocalDateTime rangeStart,
                                                              LocalDateTime rangeEnd,
                                                              int from,
                                                              int size);

}
