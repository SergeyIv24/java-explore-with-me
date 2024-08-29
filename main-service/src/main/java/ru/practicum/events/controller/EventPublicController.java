package ru.practicum.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.GeneralConstants;
import ru.practicum.dto.StatisticDto;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.services.EventsServicePublic;
import ru.practicum.statistic.StatisticClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    private final EventsServicePublic eventService;
    private final StatisticClient statisticClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventRespShort> searchEvents(@RequestParam(value = "text", required = false) String text,
                                                   @RequestParam(value = "categories", required = false)
                                                   List<Integer> categories,
                                                   @RequestParam(value = "paid", required = false) Boolean paid,
                                                   @RequestParam(value = "rangeStart", required = false)
                                                   String rangeStart,
                                                   @RequestParam(value = "rangeEnd", required = false)
                                                   String rangeEnd,
                                                   @RequestParam(value = "onlyAvailable", required = false,
                                                           defaultValue = "false") boolean onlyAvailable,
                                                   @RequestParam(value = "sort", required = false) String sort,
                                                   @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @Min(0) @RequestParam(value = "size", defaultValue = "10") int size,
                                                   HttpServletRequest httpServletRequest) {

        String ip = httpServletRequest.getRemoteAddr();
        String path = httpServletRequest.getRequestURI();

        log.info("EventPublicController, searchEvents, search parameters: text: {}, categories: {}, paid: {}," +
                        "rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("EventPublicController, searchEvents. Requester IP: {}, path: {}", ip, path);

        StatisticDto statisticDto = prepareStatisticDto("ewm-main-service", path, ip);
        ResponseEntity<Object> response = statisticClient.addStat(statisticDto);

        validateResponses(response);

        log.info("EventPublicController, getEvent. Statistic was sent to stats-server, statisticDto: {}",
                statisticDto);

        LocalDateTime start = convertToLocalDataTime(decode(rangeStart));
        LocalDateTime end = convertToLocalDataTime(decode(rangeEnd));

        return eventService.searchEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventRespFull getEvent(@PathVariable("id") long eventId,
                                  HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getRemoteAddr();
        String path = httpServletRequest.getRequestURI();

        log.info("EventPublicController, getEvent, eventId: {}, requesterIp: {}, path: {}", eventId, ip, path);

        StatisticDto statisticDto = prepareStatisticDto("ewm-main-service", path, ip);
        ResponseEntity<Object> response = statisticClient.addStat(statisticDto);

        validateResponses(response);

        log.info("EventPublicController, getEvent. Statistic was sent to stats-server, statisticDto: {}",
                statisticDto);
        return eventService.getEvent(eventId, path);
    }

    private StatisticDto prepareStatisticDto(String app, String uri, String ip) {
        return StatisticDto
                .builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void validateResponses(ResponseEntity<?> response) {
        if (response.getStatusCode().is4xxClientError()) {
            log.error("EventPublicController. Status code: {}, responseBody: {}", response.getStatusCode(),
                    response.getBody());
        }

        if (response.getStatusCode().is5xxServerError()) {
            log.error("EventPublicController. Status code: {}, responseBody: {}", response.getStatusCode(),
                    response.getBody());
        }
    }

    private String decode(String parameter) {
        if (parameter == null) {
            return null;
        }
        return URLDecoder.decode(parameter, StandardCharsets.UTF_8);
    }

    private LocalDateTime convertToLocalDataTime(String date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.parse(date, GeneralConstants.DATE_FORMATTER);
    }
}
