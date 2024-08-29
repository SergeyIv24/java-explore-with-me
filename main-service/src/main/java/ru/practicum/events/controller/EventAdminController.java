package ru.practicum.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventUpdate;
import ru.practicum.events.services.EventsServiceAdmin;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventsServiceAdmin eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventRespFull adminsUpdate(@Valid @RequestBody EventUpdate eventUpdate,
                                      @PathVariable("eventId") long eventId) {
        log.info("EventAdminController, adminsUpdate. EventId: {}, eventRequest: {}", eventId, eventUpdate);
        return eventService.adminsUpdate(eventUpdate, eventId);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventRespFull> getEventsByConditionalsForAdmin(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Integer> categories,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
            @Min(0) @RequestParam(value = "size", defaultValue = "10") int size) {

        log.info("EventAdminController, getEventsByConditionalsForAdmin, users: {}, states: {}," +
                        "categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}", users, states,
                categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByConditionalsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
