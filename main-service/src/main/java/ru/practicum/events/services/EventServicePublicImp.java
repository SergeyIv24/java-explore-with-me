package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.common.GeneralConstants;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePublicImp implements EventsServicePublic {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public Collection<EventRespShort> searchEvents(String text, List<Integer> categories, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   boolean onlyAvailable, String sort, int from, int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        if (text == null) {
            text = "";
        }
        if (categories == null) {
            categories = List.of();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.parse("1000-12-12 12:12:12", GeneralConstants.DATE_FORMATTER);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.parse("3000-12-12 12:12:12", GeneralConstants.DATE_FORMATTER);
        }

        List<Event> events = eventRepository.searchEvents(text,categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable); //paid, rangeStart, rangeEnd,
        return events
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .collect(Collectors.toList());
    }

    @Override
    public EventRespFull getEvent(long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, String.valueOf(EventStates.PUBLISHED))
                .orElseThrow(() -> {
                    log.warn("Attempt to get unknown event");
                    return new NotFoundException("Event with id = " + eventId + "was not found");
                });

        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId,
                String.valueOf(RequestStatus.ACCEPTED));

        EventRespFull eventFull = EventMapper.mapToEventRespFull(event);
        eventFull.setConfirmedRequests(confirmedRequests);
        return eventFull;
    }
}
