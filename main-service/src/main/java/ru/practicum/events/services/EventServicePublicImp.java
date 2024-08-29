package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.errors.NotFoundException;
import ru.practicum.errors.ValidationException;
import ru.practicum.common.ConnectToStatServer;
import ru.practicum.common.GeneralConstants;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;
import ru.practicum.requests.dto.EventIdByRequestsCount;
import ru.practicum.statistic.StatisticClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePublicImp implements EventsServicePublic {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

    @Override
    public Collection<EventRespShort> searchEvents(String text, List<Integer> categories, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   boolean onlyAvailable, String sort, int from, int size) {
        validateDates(rangeStart, rangeEnd);
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        if (text == null) {
            text = "";
        }
        if (categories == null) {
            categories = List.of();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = GeneralConstants.defaultEndTime;
        }

        List<EventRespShort> events = eventRepository
                .searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();

        List<Long> eventsIds = events.stream()
                .map(EventRespShort::getId)
                .toList();

        Map<Long, Long> confirmedRequestsByEvents = requestRepository
                .countByEventIdInAndStatusGroupByEvent(eventsIds, String.valueOf(RequestStatus.CONFIRMED))
                .stream()
                .collect(Collectors.toMap(EventIdByRequestsCount::getEvent, EventIdByRequestsCount::getCount));

        List<Long> views = ConnectToStatServer.getViews(GeneralConstants.defaultStartTime,
                GeneralConstants.defaultEndTime, ConnectToStatServer.prepareUris(eventsIds),
                true, statisticClient);

        for (int i = 0; i < events.size(); i++) {
            if ((!views.isEmpty()) && (views.get(i) != 0)) {
                events.get(i).setViews(views.get(i));
            } else {
                events.get(i).setViews(0L);
            }
            events.get(i)
                    .setConfirmedRequests(confirmedRequestsByEvents
                            .getOrDefault(events.get(i).getId(), 0L));
        }
        return events;

    }

    @Override
    public EventRespFull getEvent(long eventId, String path) {
        Event event = eventRepository.findByIdAndState(eventId, String.valueOf(EventStates.PUBLISHED))
                .orElseThrow(() -> {
                    log.warn("Attempt to get unknown event");
                    return new NotFoundException("Event with id = " + eventId + "was not found");
                });

        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId,
                String.valueOf(RequestStatus.CONFIRMED));

        EventRespFull eventFull = EventMapper.mapToEventRespFull(event);
        eventFull.setConfirmedRequests(confirmedRequests);
        List<Long> views = ConnectToStatServer.getViews(GeneralConstants.defaultStartTime,
                GeneralConstants.defaultEndTime, path,
                true, statisticClient);
        if (views.isEmpty()) {
            eventFull.setViews(0L);
        }
        eventFull.setViews(views.get(0));
        return eventFull;
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return;
        }
        if (start.isAfter(end)) {
            log.warn("Prohibited. Start is after end. Start: {}, end: {}", start, end);
            throw new ValidationException("Event must be published");
        }
    }
}
