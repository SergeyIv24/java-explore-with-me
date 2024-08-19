package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventRequest;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventRequest adminsUpdate(@Valid @RequestBody EventRequest eventRequest,
                                       @PathVariable("eventId") long eventId) {
        return eventService.adminsUpdate(eventRequest, eventId);
    }


}
