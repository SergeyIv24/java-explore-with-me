package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ClientException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.common.GeneralConstants;
import ru.practicum.dto.StatisticResponse;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;
import ru.practicum.statistic.StatisticClient;

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
    private final StatisticClient statisticClient;

    private final LocalDateTime defaultStartTime = LocalDateTime.parse("1000-12-12 12:12:12",
            GeneralConstants.DATE_FORMATTER);
    private final LocalDateTime defaultEndTime = LocalDateTime.parse("3000-12-12 12:12:12",
            GeneralConstants.DATE_FORMATTER);

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
            rangeStart = LocalDateTime.now().plusMinutes(1L);
        }
        if (rangeEnd == null) {
            rangeEnd = defaultEndTime;
        }

        List<EventRespShort> events = eventRepository
                .searchEvents(text,categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();

        List<Long> eventsIds = events.stream()
                .map(EventRespShort::getId)
                .toList();



        List<Long> confirmedRequests = requestRepository.countByEventIdInAndStatusGroupByEvent(eventsIds,
                String.valueOf(RequestStatus.CONFIRMED));

        if (confirmedRequests.isEmpty()) {
            return events;
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setConfirmedRequests(confirmedRequests.get(i));
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
        eventFull.setViews(getViews(defaultStartTime, defaultEndTime, List.of(path)).get(0));
        return eventFull;
    }

    private List<String> prepareUris(List<Long> ids) {
        return ids
                .stream()
                .map((id) -> "event/" + id)
                .collect(Collectors.toList());
    }

    private List<Long> getViews(LocalDateTime start, LocalDateTime end, List<String> uris) {
        ResponseEntity<Object> response = statisticClient.getStats(start, end, uris, false);

        if (response.getStatusCode().is4xxClientError()) {
            log.warn("Bad request. Status code is {}", response.getStatusCode());
            throw new ClientException("Bad request. Status code is: " + response.getStatusCode());
        }

        if (response.getStatusCode().is5xxServerError()) {
            log.warn("Internal server error statusCode is {}", response.getStatusCode());
            throw new ClientException("Internal server error statusCode is " + response.getStatusCode());
        }

        if (response.getBody() == null) {
            log.warn("Returned empty body");
            throw new ClientException("Returned empty body");
        }
        List<StatisticResponse> statisticResponse = (List<StatisticResponse>) response.getBody();
        return statisticResponse
                .stream()
                .map(StatisticResponse::getHits)
                .collect(Collectors.toList());
    }
}
