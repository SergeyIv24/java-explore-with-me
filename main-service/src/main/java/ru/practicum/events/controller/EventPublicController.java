package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.services.EventsServicePublic;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    private final EventsServicePublic eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventRespShort> searchEvents(@RequestParam(value = "text", required = false) String text,
                                                   @RequestParam (value = "categories", required = false)
                                                        List<Integer> categories,
                                                   @RequestParam(value = "paid", required = false) Boolean paid,
                                                   @RequestParam(value = "rangeStart", required = false)
                                                       LocalDateTime rangeStart,
                                                   @RequestParam(value = "rangeEnd", required = false)
                                                       LocalDateTime rangeEnd,
                                                   @RequestParam(value = "onlyAvailable", required = false,
                                                           defaultValue = "false") boolean onlyAvailable,
                                                   @RequestParam(value = "sort", required = false) String sort,
                                                   @RequestParam(value = "from", required = false,
                                                           defaultValue = "0") int from,
                                                   @RequestParam(value = "size", required = false,
                                                           defaultValue = "10") int size) {

        log.info("EventPublicController, searchEvents, search parameters: text: {}, categories: {}, paid: {}," +
                "rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public EventRespFull getEvent(@PathVariable("id") long eventId) {
        log.info("EventPublicController, getEvent, eventId: {}", eventId);
        return null;
    }

}
