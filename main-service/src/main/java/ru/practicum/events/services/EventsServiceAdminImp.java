package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ConflictException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.common.GeneralConstants;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.LocationRepository;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventUpdate;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;

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
    private final RequestRepository requestRepository;

    @Override
    public EventRespFull adminsUpdate(EventUpdate eventUpdate, long eventId) {

        Event updatingEvent = validateAndGetEvent(eventId);

        checkAbilityToUpdate(updatingEvent);

        Category category = updatingEvent.getCategory();

        if (eventUpdate.getCategory() != null) {
            category = validateAndGetCategory(eventUpdate.getCategory());
        }

        if (eventUpdate.getLocation() != null) {
            addLocation(eventUpdate.getLocation());
        }

        Event updatedEvent = eventRepository.save(EventMapper
                .updateEvent(updatingEvent, eventUpdate, category));
        long confirmedRequests = requestRepository
                .countByEventIdAndStatus(eventId, String.valueOf(RequestStatus.CONFIRMED));
        EventRespFull eventFull = EventMapper.mapToEventRespFull(updatedEvent);
        eventFull.setConfirmedRequests(confirmedRequests);
        return eventFull;
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

    private void checkAbilityToUpdate(Event event) {

        if (event.getState().equals(String.valueOf(EventStates.PUBLISHED))
                || event.getState().equals(String.valueOf(EventStates.CANCELED))) {
            log.warn("Update is prohibited. event stat: {}", event.getState());
            throw new ConflictException("States must be" + EventStates.PENDING + " or " + EventStates.CANCELED);
        }

/*        if (!(event.getState().equals(String.valueOf(EventStates.PENDING)))
                && (event.getState().equals(String.valueOf(EventStates.CANCELED)))) {
            log.warn("Update is prohibited. event stat: {}", event.getState());
            throw new ConflictException("States must be" + EventStates.PENDING + " or " + EventStates.CANCELED);
        }*/
    }
}
