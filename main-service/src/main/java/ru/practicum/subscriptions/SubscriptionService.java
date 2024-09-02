package ru.practicum.subscriptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.ConnectToStatServer;
import ru.practicum.common.GeneralConstants;
import ru.practicum.errors.ConflictException;
import ru.practicum.errors.NotFoundException;
import ru.practicum.events.EventMapper;
import ru.practicum.events.EventRepository;
import ru.practicum.events.EventStates;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.RequestStatus;
import ru.practicum.requests.dto.EventIdByRequestsCount;
import ru.practicum.statistic.StatisticClient;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.subscriptions.model.Subscription;
import ru.practicum.users.UserMapper;
import ru.practicum.users.UserRepository;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

    @Override
    public SubscriptionDto subscribeToUser(long userId, long followerId) {
        User user = validateAndGetUser(userId);
        User follower = validateAndGetUser(followerId);
        validateSubscription(userId, followerId);
        Subscription addingSubscription = new Subscription();
        addingSubscription.setUser(user);
        addingSubscription.setFollower(follower);
        return SubscriptionMapper.mapToSubscriptionDto(subscriptionRepository.save(addingSubscription));
    }

    @Override
    public void cancelSubscribe(long userId, long followerId) {
        validateAndGetUser(userId);
        validateAndGetUser(followerId);
        Subscription subscription = validateAndGetSubscription(userId, followerId);
        subscriptionRepository.deleteById(subscription.getSubscriptionId());
    }

    @Override
    public List<UserDto> getUsersIFollow(long userId) {
        validateAndGetUser(userId);
        return subscriptionRepository
                .findByFollowerId(userId)
                .stream()
                .map((userProjection) ->
                        new User(userProjection.getId(), userProjection.getEmail(), userProjection.getName()))
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> getMyFollowers(long userId) {
        validateAndGetUser(userId);
        return subscriptionRepository
                .findByUserId(userId)
                .stream()
                .map((userProjection) ->
                        new User(userProjection.getId(), userProjection.getEmail(), userProjection.getName()))
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<EventRespShort> getUsersEvents(long userId, int from, int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);
        List<EventRespShort> events = eventRepository
                .findByInitiatorIdAndState(userId, String.valueOf(EventStates.PUBLISHED), pageable)
                .stream()
                .map(EventMapper::mapToEventRespShort)
                .toList();

        List<Long> eventsIds = events
                .stream()
                .map(EventRespShort::getId)
                .toList();

        Map<Long, Long> confirmedRequestsByEvents = requestRepository
                .countByEventIdInAndStatusGroupByEvent(eventsIds, String.valueOf(RequestStatus.CONFIRMED))
                .stream()
                .collect(Collectors.toMap(EventIdByRequestsCount::getEvent, EventIdByRequestsCount::getCount));

        List<Long> views = ConnectToStatServer.getViews(GeneralConstants.defaultStartTime, GeneralConstants.defaultEndTime,
                ConnectToStatServer.prepareUris(eventsIds), true, statisticClient);

        for (int i = 0; i < events.size(); i++) {

            if ((!views.isEmpty()) && (views.get(i) != 0)) {
                events.get(i).setViews(views.get(i));
            } else {
                events.get(i).setViews(0L);
            }
            events.get(i)
                    .setConfirmedRequests(confirmedRequestsByEvents
                            .getOrDefault(events.get(i).getId(), 0L));
        }
        return events;
    }

    private void validateSubscription(long userId, long followerId) {
        if (userId == followerId) {
            log.warn("User with userId: {} tried to follow to himself(followerId: {})", userId, followerId);
            throw new ConflictException("User with userId: " + userId + " tried to follow to himself(followerId: "
                    + followerId + ")");
        }
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndFollowerId(userId, followerId);
        if (subscription.isPresent()) {
            log.warn("User with id: {} have already subscribed to user with id: {}", followerId, userId);
            throw new ConflictException("User with id: " + followerId +
                    " have already subscribed to user with id: " + userId);
        }
    }

    private User validateAndGetUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Attempt to delete unknown user");
            throw new NotFoundException("User with id = " + userId + " was not found");
        }
        return user.get();
    }

    private Subscription validateAndGetSubscription(long userId, long followerId) {
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndFollowerId(userId, followerId);
        if (subscription.isEmpty()) {
            log.warn("User with id: {} does not subscribe to user with id: {}", followerId, userId);
            throw new NotFoundException("User with id: " + followerId +
                    " does not subscribe to user with id: " + userId);
        }
        return subscription.get();
    }
}
