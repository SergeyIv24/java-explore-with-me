package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ConflictException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestsForConfirmation;
import ru.practicum.requests.model.Requests;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventRequest createEvent(EventRequest eventRequest, long userId) {

        if (eventRequest.getRequestModeration() == null) {
            eventRequest.setRequestModeration(true);
        }
        if (eventRequest.getPaid() == null) {
            eventRequest.setPaid(false);
        }
        if (eventRequest.getParticipantLimit() == null) {
            eventRequest.setParticipantLimit(0);
        }

        validateEventDate(eventRequest.getEventDate());

        addLocation(eventRequest.getLocation()); //Adding locations to database because location is separated entity

        Event addingEvent = EventMapper.mapToEvent(eventRequest);

        addingEvent.setInitiator(validateAndGetUser(userId));
        addingEvent.setCategory(validateAndGetCategory(eventRequest.getCategory()));
        addingEvent.setCreatedOn(LocalDateTime.now());
        addingEvent.setState(String.valueOf(EventStates.WAITING));
        return EventMapper.mapToEventRequest(eventRepository.save(addingEvent));
    }

    @Override
    public Collection<EventRespShort> getUsersEvents(long userId, int from, int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        List<EventRespShort> events = eventRepository.findByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();

        List<Long> eventIds = events.stream().map(EventRespShort::getId).toList();

        List<Long> confirmedRequests = requestRepository.countByEventIdInAndStatusGroupByEvent(eventIds,
                String.valueOf(RequestStatus.ACCEPTED));

        if (confirmedRequests.isEmpty()) {
            return events;
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setConfirmedRequests(confirmedRequests.get(i));
        }

        return events;
    }

    @Override
    public EventRespFull getUsersFullEventById(long userId, long eventId) {
        Event event = validateAndGetEvent(eventId);
        long confirmedRequests = requestRepository
                .countByEventIdAndStatus(eventId, String.valueOf(RequestStatus.ACCEPTED));
        EventRespFull eventRespFull = EventMapper.mapToEventRespFull(event);
        eventRespFull.setConfirmedRequests(confirmedRequests);
        return eventRespFull;
    }

    @Override
    public EventRequest updateUsersEvent(long userId, long eventId, EventRequest eventRequest) {

        checkAbilityToUpdate(eventRequest);
        validateEventDate(eventRequest.getEventDate());
        Event updatingEvent = validateAndGetEvent(eventId);

        updatingEvent.setAnnotation(eventRequest.getAnnotation());
        updatingEvent.setCategory(validateAndGetCategory(eventRequest.getCategory()));
        updatingEvent.setDescription(eventRequest.getDescription());
        updatingEvent.setEventDate(eventRequest.getEventDate());
        updatingEvent.setLocation(eventRequest.getLocation());
        updatingEvent.setPaid(eventRequest.getPaid());
        updatingEvent.setParticipantLimit(eventRequest.getParticipantLimit());
        updatingEvent.setTitle(eventRequest.getTitle());

        return EventMapper.mapToEventRequest(eventRepository.save(updatingEvent));
    }

/*    @Override
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
    }*/

    @Override
    public Collection<RequestDto> getRequestsByEventId(long eventId, long userId) {
        validateAndGetEvent(eventId);
        return requestRepository.findByEventId(eventId)
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<RequestDto> approveRequests(RequestsForConfirmation requestsForConfirmation,
                                                  long userId,
                                                  long eventId) {
        Event event = validateAndGetEvent(eventId); //checking event availability
        int participants = countParticipants(eventId); //Approved participants
        checkParticipantsLimit(event.getParticipantLimit(), participants); //Check possibility to add
        int freeSlots = event.getParticipantLimit() - participants; //Amount participants who can be added
        List<Requests> requests = requestRepository
                .findByIdInAndEventId(requestsForConfirmation.getRequestIds(), eventId); //Updating requests for event
        checkRequestStatus(requests); //Checking request`s status cause all requests should be PENDING  either CONFLICT

        List<Requests> requestsToApprove = requests.subList(0, (freeSlots + 1));

        List<Requests> requestsToCancel = requests.subList((freeSlots + 1), (requests.size() - 1));

        for (Requests requestToApprove : requestsToApprove) {
            requestToApprove.setStatus(String.valueOf(RequestStatus.ACCEPTED));
        }

        for (Requests requestToCancel : requestsToCancel) {
            requestToCancel.setStatus(String.valueOf(RequestStatus.CANCELED));
        }

        requestRepository.saveAll(requestsToCancel);
        return requestRepository.saveAll(requestsToApprove)
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

/*    @Override
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
    }*/


    private int countParticipants(long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId,
                String.valueOf(RequestStatus.ACCEPTED));
    }

    private void checkParticipantsLimit(long participantsLimit, long participants) {
        if (participantsLimit <= (participants + 1)) {
            log.warn("Unable to add request. ParticipantLimit {} less then request amount {}",
                    participantsLimit, (participants + 1));
            throw new ConflictException("Exceeded requesters amount");
        }
    }

    //Two pointers to checking request`s status
    private void checkRequestStatus(List<Requests> request) {

        int leftIdx = 0;
        int rightIdx = request.size() - 1;

        while (leftIdx < rightIdx) {

            if (!request.get(leftIdx).getStatus().equals(RequestStatus.PENDING.name())) {
                log.warn(""); //todo
                throw new ConflictException("Request with id = " + request.get(leftIdx).getId() + " has status: "
                        + request.get(leftIdx).getStatus());
            }

            if (!request.get(rightIdx).getStatus().equals(RequestStatus.PENDING.name())) {
                log.warn("");
                throw new ConflictException("Request with id = " + request.get(rightIdx).getId() + " has status: "
                        + request.get(rightIdx).getStatus());
            }
            leftIdx++;
            rightIdx--;
        }
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

    private Event validateAndGetEvent(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            log.warn("Attempt to get unknown event");
            throw new NotFoundException("Event with id = " + eventId + "was not found");
        }
        return event.get();
    }

    private User validateAndGetUser(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
        return user.get();
    }

    private Category validateAndGetCategory(int categoryId) {
        Optional<Category> category = categoriesRepository.findById(categoryId);

        if (category.isEmpty()) {
            log.warn("Attempt to delete unknown category with categoryId: {}", categoryId);
            throw new NotFoundException("Category with id = " + categoryId + " was not found");
        }
        return category.get();
    }

    private void checkAbilityToUpdate(EventRequest eventRequest) {
        if (!(eventRequest.getState().equals(String.valueOf(EventStates.WAITING)))
                && !(eventRequest.getState().equals(String.valueOf(EventStates.CANCELED)))) {
            log.warn("Update is prohibited. eventRequest stat: {}", eventRequest.getState());
            throw new ConflictException("States must be" + EventStates.WAITING + " or " + EventStates.CANCELED);
        }
    }
}
