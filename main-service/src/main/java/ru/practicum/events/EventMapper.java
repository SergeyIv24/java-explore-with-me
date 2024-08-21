package ru.practicum.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event mapToEvent(EventRequest eventRequest) {
        return Event
                .builder()
                .id(eventRequest.getId())
                .annotation(eventRequest.getAnnotation())
                .description(eventRequest.getDescription())
                .eventDate(eventRequest.getEventDate())
                .location(eventRequest.getLocation())
                .paid(eventRequest.getPaid())
                .participantLimit(eventRequest.getParticipantLimit())
                .requestModeration(eventRequest.getRequestModeration())
                .title(eventRequest.getTitle())
                .build();
    }

    public static EventRequest mapToEventRequest(Event event) {
        return EventRequest
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory().getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .initiator(event.getId())
                .requestModeration(event.getRequestModeration())
                .createdOn(event.getCreatedOn())
                .state(event.getState())
                .build();
    }

    public static EventRespShort mapToEventRespShort(Event event) {
        return EventRespShort
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();

    }

    public static EventRespFull mapToEventRespFull(Event event) {
        return EventRespFull
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .initiator(event.getInitiator())
                .requestModeration(event.getRequestModeration())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .build();
    }
}
