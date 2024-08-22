package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.common.GeneralConstants;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.LocationRepository;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsServiceAdminImp implements EventsServiceAdmin {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;

    @Override
    public EventRequest adminsUpdate(EventRequest eventRequest, long eventId) {
        Event updatingEvent = validateAndGetEvent(eventId);

        updatingEvent.setAnnotation(eventRequest.getAnnotation());
        updatingEvent.setCategory(validateAndGetCategory(eventRequest.getCategory()));
        updatingEvent.setDescription(eventRequest.getDescription());
        updatingEvent.setEventDate(eventRequest.getEventDate());
        updatingEvent.setLocation(eventRequest.getLocation());
        updatingEvent.setPaid(eventRequest.getPaid());
        updatingEvent.setParticipantLimit(eventRequest.getParticipantLimit());
        updatingEvent.setRequestModeration(eventRequest.getRequestModeration());
        updatingEvent.setState(eventRequest.getState());
        addLocation(updatingEvent.getLocation());

        return EventMapper.mapToEventRequest(eventRepository.save(updatingEvent));
    }

    @Override
    public Collection<EventRespFull> getEventsByConditionalsForAdmin(List<Long> users,
                                                                     List<String> states,
                                                                     List<Integer> categories,
                                                                     LocalDateTime rangeStart,
                                                                     LocalDateTime rangeEnd,
                                                                     int from,
                                                                     int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        if (states == null) {
            states = List.of();
        }
        if (categories == null) {
            categories = List.of();
        }
        if (users == null) {
            users = List.of();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.parse("1000-12-12 12:12:12", GeneralConstants.DATE_FORMATTER);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.parse("3000-12-12 12:12:12", GeneralConstants.DATE_FORMATTER);
        }

        return eventRepository.findByConditionals(states, categories, users, rangeStart, rangeEnd, pageable)
                .stream()
                .map(EventMapper::mapToEventRespFull)
                .collect(Collectors.toList());
    }

    private void addLocation(Location location) {
        locationRepository.save(location);
    }

    private Event validateAndGetEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            log.warn("Attempt to get unknown event");
            throw new NotFoundException("Event with id = " + eventId + "was not found");
        }
        return event.get();
    }

    private Category validateAndGetCategory(int categoryId) {
        Optional<Category> category = categoriesRepository.findById(categoryId);

        if (category.isEmpty()) {
            log.warn("Attempt to delete unknown category with categoryId: {}", categoryId);
            throw new NotFoundException("Category with id = " + categoryId + " was not found");
        }
        return category.get();
    }
}
