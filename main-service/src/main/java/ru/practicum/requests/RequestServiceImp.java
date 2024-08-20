package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ConflictException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.model.Event;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.model.Requests;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDto addRequest(long eventId, long userId) {
        Requests addingRequest = Requests
                .builder()
                .created(LocalDateTime.now())
                .build();
        Event event = validateEvent(eventId); //Check event
        addingRequest.setRequester(validateUser(userId)); //Check and set user
        checkAbilityToAddRequest(event, addingRequest, userId);

        addingRequest.setEvent(event);
        if (!event.getRequestModeration()) {
            addingRequest.setStatus(String.valueOf(RequestStatus.ACCEPTED));
        }

        addingRequest.setStatus(String.valueOf(RequestStatus.PENDING));

        return RequestMapper.mapToRequestDto(requestRepository.save(addingRequest));
    }

    @Override
    public RequestDto cancelRequest(long requestId, long userId) {
        Requests updatingRequest = validateRequest(requestId);
        updatingRequest.setStatus(String.valueOf(RequestStatus.CANCELED));
        return RequestMapper.mapToRequestDto(requestRepository.save(updatingRequest));
    }

    @Override
    public Collection<RequestDto> getMyRequests(long userId) {
        return requestRepository
                .findByRequesterId(userId)
                .stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toSet());
    }

    private void checkAbilityToAddRequest(Event event, Requests request, Long requesterId) {
        isThereDuplicate(requesterId, request);
        validateEventStatesAndRequesterId(event, requesterId);
        checkParticipantsLimit(event);
    }

    private void validateEventStatesAndRequesterId(Event event, Long requesterId) {
        if (event.getState().equals(String.valueOf(EventStates.WAITING))
                || event.getState().equals(String.valueOf(EventStates.CANCELED))) {
            log.warn("Event with EventId: {} is not published. Request is canceled", event.getId());
            throw new ConflictException("Event with id = " + event.getId() + " is not published");
        }

        if (event.getInitiator().getId().equals(requesterId)) {
            log.warn("Attempt to add request to self event");
            throw new ConflictException("Could not add request to event");
        }
    }

    private void checkParticipantsLimit(Event event) {
        long requestAmountForEvent = requestRepository.countByEventId(event.getId());

        if (event.getParticipantLimit() < (requestAmountForEvent + 1)) {
            log.warn("Unable to add request. ParticipantLimit {} less then request amount {}",
                    event.getParticipantLimit(), (requestAmountForEvent + 1));
            throw new ConflictException("Exceeded requesters amount");
        }
    }

    private void isThereDuplicate(Long requesterId, Requests target) {
        Set<Requests> requests = requestRepository.findByRequesterId(requesterId);

        if (requests.contains(target)) {
            log.warn("Request is duplicate");
            throw new ConflictException("Request is duplicate");
        }
    }

    private Requests validateRequest(long requestId) {
        Optional<Requests> request = requestRepository.findById(requestId);

        if (request.isEmpty()) {
            log.warn("Attempt to get unknown event");
            throw new NotFoundException("Request with id = " + request + "was not found");
        }
        return request.get();
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
}

/*    private static List sortBy() {
        return null;
    }

    private static <T extends Requests> boolean binarySearch(List<T> collection, T target) {
        collection.sort(Comparator.comparing((Requests req) -> req.getRequester().getId()));

        collection.sort((Requests req1, Requests req2) -> {
            if (req1.getRequester().getId() > req2.getRequester().getId()) {
                return 1;
            } else if (req1.getRequester().getId().equals(req2.getRequester().getId())) {
                return req1.getEvent().getId().compareTo(req2.getEvent().getId());
            } else {
                return -1;
            }
        });

        int low = 0;
        int high = collection.size() - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (collection.get(mid).getRequester().getId().equals(target.getRequester().getId())) {
                if (collection.get(mid).equals(target)) {
                    return true;
                } else {
                    if (collection.get(mid).getEvent().getId() < target.getEvent().getId()) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }

            } else if (collection.get(mid).getRequester().getId() < target.getRequester().getId()) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }*/
