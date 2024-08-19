package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ConflictException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;

    @Override
    public EventRequest createEvent(EventRequest eventRequest, long userId) {
        validateEventDate(eventRequest.getEventDate());

        addLocation(eventRequest.getLocation()); //Adding locations to database because location is separated entity

        Event addingEvent = EventMapper.mapToEvent(eventRequest);

        addingEvent.setInitiator(validateUser(userId));
        addingEvent.setCategory(validateCategory(eventRequest.getCategory()));
        addingEvent.setCreatedOn(LocalDateTime.now());
        addingEvent.setState(String.valueOf(States.WAITING));
        return EventMapper.mapToEventRequest(eventRepository.save(addingEvent));
    }

    @Override
    public Collection<EventRespShort> getUsersEvents(long userId) {
        return null;
    }

    @Override
    public EventFull getUsersFullEvent(long userId, long eventId) {
        validateEvent(eventId);
        validateUser(userId);
        return null;
    }

    @Override
    public EventRequest updateUsersEvent(long userId, long eventId, EventRequest eventRequest) {
        checkAbilityToUpdate(eventRequest);
        validateEventDate(eventRequest.getEventDate());
        Event updatingEvent = validateEvent(eventId);

        //updatingEvent.setId(eventRequest.getId());
        updatingEvent.setAnnotation(eventRequest.getAnnotation());
        updatingEvent.setCategory(validateCategory(eventRequest.getCategory()));
        updatingEvent.setDescription(eventRequest.getDescription());
        updatingEvent.setEventDate(eventRequest.getEventDate());
        updatingEvent.setLocation(eventRequest.getLocation());
        updatingEvent.setPaid(eventRequest.getPaid());
        updatingEvent.setParticipantLimit(eventRequest.getParticipantLimit());
        //updatingEvent.setRequestModeration(eventRequest.getRequestModeration());
        updatingEvent.setTitle(eventRequest.getTitle());

        return EventMapper.mapToEventRequest(eventRepository.save(updatingEvent));
    }

    @Override
    public EventRequest adminsUpdate(EventRequest eventRequest, long eventId) {
        Event updatingEvent = validateEvent(eventId);

        updatingEvent.setAnnotation(eventRequest.getAnnotation());
        updatingEvent.setCategory(validateCategory(eventRequest.getCategory()));
        updatingEvent.setDescription(eventRequest.getDescription());
        updatingEvent.setEventDate(eventRequest.getEventDate());
        updatingEvent.setLocation(eventRequest.getLocation());
        updatingEvent.setPaid(eventRequest.getPaid());
        updatingEvent.setParticipantLimit(eventRequest.getParticipantLimit());
        updatingEvent.setRequestModeration(eventRequest.getRequestModeration());
        updatingEvent.setState(eventRequest.getState());

        return EventMapper.mapToEventRequest(eventRepository.save(updatingEvent));
    }

    private void addLocation(Location location) {
        locationRepository.save(location);
    }

    private void validateEventDate(LocalDateTime eventDate) {

        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Event data ({}) is before then now + 2 hours", eventDate);
            throw new ConflictException("Event data " + eventDate + " is before then now + 2 hours");
        }
    }

    private Event validateEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            log.warn("Attempt to get unknown event");
            throw new NotFoundException("Event with id = " + eventId + "was not found");
        }
        return event.get();
    }

    private User validateUser(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
        return user.get();
    }

    private Category validateCategory(int categoryId) {
        Optional<Category> category = categoriesRepository.findById(categoryId);

        if (category.isEmpty()) {
            log.warn("Attempt to delete unknown category with categoryId: {}", categoryId);
            throw new NotFoundException("Category with id = " + categoryId + " was not found");
        }
        return category.get();
    }

    private void checkAbilityToUpdate(EventRequest eventRequest) {
        if (!(eventRequest.getState().equals(String.valueOf(States.WAITING)))
                && !(eventRequest.getState().equals(String.valueOf(States.CANCELED)))) {
            log.warn("Update is prohibited. eventRequest stat: {}", eventRequest.getState());
            throw new ConflictException("States must be" + States.WAITING + " or " + States.CANCELED);
        }
    }
}
