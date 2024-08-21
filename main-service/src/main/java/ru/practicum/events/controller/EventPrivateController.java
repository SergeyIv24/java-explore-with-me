package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestsForConfirmation;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequest createEvent(@Valid @RequestBody EventRequest eventRequest,
                                    @PathVariable(value = "userId") long userId) {
        log.info("EventPrivateController, createEvent. UserId: {}, eventRequest: {}",
                userId, eventRequest.toString());
        return eventService.createEvent(eventRequest, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventRespShort> getUsersEvents(@PathVariable(value = "userId") long userId,
                                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("EventPrivateController, getUsersEvents. UserId: {}", userId);
        return eventService.getUsersEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventRespFull getUsersFullEventById(@PathVariable(value = "userId") long userId,
                                               @PathVariable(value = "eventId") long eventId) {
        log.info("EventPrivateController, getUsersFullEvent. UserId: {}, eventId: {}", userId, eventId);
        return eventService.getUsersFullEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventRequest updateUsersEvent(@PathVariable(value = "userId") long userId,
                                         @PathVariable(value = "eventId") long eventId,
                                         @Valid @RequestBody EventRequest eventRequest) {
        log.info("EventPrivateController, updateUsersEvent. UserId: {}, eventId: {}", userId, eventId);
        return eventService.updateUsersEvent(userId, eventId, eventRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestDto> getRequestsByEvenId(@PathVariable(value = "userId") long userId,
                                                      @PathVariable(value = "eventId") long eventId) {
        log.info("EventPrivateController, getRequestsByEvenId. UserId: {}, eventId: {}", userId, eventId);
        return eventService.getRequestsByEventId(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestDto> approveRequest(@Valid @RequestBody RequestsForConfirmation requestsForConfirmation,
                                                 @PathVariable(value = "userId") long userId,
                                                 @PathVariable(value = "eventId") long eventId) {
        log.info("EventPrivateController, approveRequest. UserId: {}, eventId: {}", userId, eventId);
        return eventService.approveRequests(requestsForConfirmation, userId, eventId);
    }
}
