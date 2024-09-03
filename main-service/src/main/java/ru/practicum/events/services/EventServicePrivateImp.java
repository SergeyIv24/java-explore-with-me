package ru.practicum.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.Utilities;
import ru.practicum.errors.ConflictException;
import ru.practicum.errors.NotFoundException;
import ru.practicum.categories.CategoriesRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.common.ConnectToStatServer;
import ru.practicum.common.GeneralConstants;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.LocationRepository;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.dto.EventRequest;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.events.dto.EventUpdate;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;
import ru.practicum.requests.dto.EventIdByRequestsCount;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestResponse;
import ru.practicum.requests.dto.RequestsForConfirmation;
import ru.practicum.requests.model.Requests;
import ru.practicum.statistic.StatisticClient;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImp implements EventServicePrivate {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

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
        addingEvent.setState(String.valueOf(EventStates.PENDING));

        Event saved = eventRepository.save(addingEvent);
        return EventMapper.mapToEventRequest(saved);
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

        Map<Long, Long> confirmedRequestsByEvents = requestRepository
                .countByEventIdInAndStatusGroupByEvent(eventIds, String.valueOf(RequestStatus.CONFIRMED))
                .stream()
                .collect(Collectors.toMap(EventIdByRequestsCount::getEvent, EventIdByRequestsCount::getCount));

        List<Long> views = ConnectToStatServer.getViews(GeneralConstants.defaultStartTime, GeneralConstants.defaultEndTime,
                ConnectToStatServer.prepareUris(eventIds), true, statisticClient);

        List<? extends EventRespShort> eventsForResp =
                Utilities.addViewsAndConfirmedRequests(events, confirmedRequestsByEvents, views);

        return Utilities.checkTypes(eventsForResp,
                EventRespShort.class);
    }

    @Override
    public EventRespFull getUsersFullEventById(long userId, long eventId, String path) {
        Event event = validateAndGetEvent(eventId);
        long confirmedRequests = requestRepository
                .countByEventIdAndStatus(eventId, String.valueOf(RequestStatus.CONFIRMED));
        EventRespFull eventRespFull = EventMapper.mapToEventRespFull(event);
        eventRespFull.setConfirmedRequests(confirmedRequests);
        List<Long> views = ConnectToStatServer.getViews(GeneralConstants.defaultStartTime,
                GeneralConstants.defaultEndTime, path, true, statisticClient);
        if (views.isEmpty()) {
            eventRespFull.setViews(0L);
            return eventRespFull;
        }
        eventRespFull.setViews(views.get(0));
        return eventRespFull;
    }

    @Override
    public EventRequest updateUsersEvent(long userId, long eventId, EventUpdate eventUpdate) {
        Event updatingEvent = validateAndGetEvent(eventId);
        checkAbilityToUpdate(updatingEvent);

        if (eventUpdate.getEventDate() != null) {
            validateEventDate(eventUpdate.getEventDate());
        }

        Category category = updatingEvent.getCategory();
        if (eventUpdate.getCategory() != null) {
            category = validateAndGetCategory(eventUpdate.getCategory());
        }

        Event updatedEvent = eventRepository.save(EventMapper.updateEvent(updatingEvent, eventUpdate, category));
        return EventMapper.mapToEventRequest(updatedEvent);
    }

    @Override
    public Collection<RequestDto> getRequestsByEventId(long eventId, long userId) {
        validateAndGetEvent(eventId);
        return requestRepository.findByEventId(eventId)
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestResponse approveRequests(RequestsForConfirmation requestsForConfirmation,
                                           long userId,
                                           long eventId) {
        Event event = validateAndGetEvent(eventId); //checking event availability

        List<Requests> requests = requestRepository
                .findByIdInAndEventId(requestsForConfirmation.getRequestIds(), eventId); //Updating requests for event

        checkRequestStatus(requests); //Checking request`s status cause all requests should be PENDING  either CONFLICT

        int participants = countParticipants(eventId); //Approved participants
        checkParticipantsLimit(event.getParticipantLimit(), participants); //Check possibility to add
        int freeSlots = event.getParticipantLimit() - participants; //Amount participants who can be added

        if (freeSlots >= requests.size()) {
            List<RequestDto> approvedRequest = requestRepository.saveAll(setStatusToRequests(RequestStatus
                            .valueOf(requestsForConfirmation.getStatus()), requests))
                    .stream()
                    .map(RequestMapper::mapToRequestDto)
                    .toList();
            RequestResponse response = RequestResponse.builder().build();
            if (requestsForConfirmation.getStatus().equals(String.valueOf(RequestStatus.REJECTED))) {
                response.setRejectedRequests(approvedRequest);
                response.setConfirmedRequests(List.of());
            } else {
                response.setRejectedRequests(List.of());
                response.setConfirmedRequests(approvedRequest);
            }
            return response;
        }

        List<Requests> requestsToCancel = setStatusToRequests(RequestStatus.REJECTED,
                requests.subList(freeSlots, requests.size()));
        requestRepository.saveAll(requestsToCancel);

        List<RequestDto> confirmed = requestRepository.saveAll(setStatusToRequests(RequestStatus
                        .valueOf(requestsForConfirmation.getStatus()), requests.subList(0, freeSlots)))
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .toList();

        List<RequestDto> rejected = requestRepository.saveAll(setStatusToRequests(RequestStatus.REJECTED,
                        requests.subList(freeSlots, requests.size())))
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .toList();

        return RequestResponse
                .builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }

    private List<Requests> setStatusToRequests(RequestStatus status, List<Requests> requests) {
        for (Requests requestToApprove : requests) {
            requestToApprove.setStatus(String.valueOf(status));
        }
        return requests;
    }

    private int countParticipants(long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId,
                String.valueOf(RequestStatus.CONFIRMED));
    }

    private void checkParticipantsLimit(long participantsLimit, long participants) {
        if (participantsLimit < (participants + 1)) {
            log.warn("Unable to add request. ParticipantLimit {} less then request amount {}",
                    participantsLimit, (participants + 1));
            throw new ConflictException("Exceeded requesters amount");
        }
    }

    //Two pointers to checking request`s status
    private void checkRequestStatus(List<Requests> request) {
        int leftIdx = 0;
        int rightIdx = request.size() - 1;
        while (leftIdx <= rightIdx) {

            if (!request.get(leftIdx).getStatus().equals(RequestStatus.PENDING.name())) {
                log.warn("Status must be PENDING");
                throw new ConflictException("Request with id = " + request.get(leftIdx).getId() + " has status: "
                        + request.get(leftIdx).getStatus());
            }

            if (!request.get(rightIdx).getStatus().equals(RequestStatus.PENDING.name())) {
                log.warn("Status must be PENDING");
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

    private void checkAbilityToUpdate(Event event) {
        if (!(event.getState().equals(String.valueOf(EventStates.PENDING)))
                && !(event.getState().equals(String.valueOf(EventStates.CANCELED)))) {
            log.warn("Update is prohibited. event stat: {}", event.getState());
            throw new ConflictException("States must be" + EventStates.PENDING + " or " + EventStates.CANCELED);
        }
    }
}
