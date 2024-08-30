package ru.practicum.subscriptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Errors.ConflictException;
import ru.practicum.Errors.NotFoundException;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.subscriptions.model.Subscription;
import ru.practicum.users.UserMapper;
import ru.practicum.users.UserRepository;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDto subscribeToUser(long userId, long followerId) {
        User user = validateAndGetUser(userId);
        User follower = validateAndGetUser(followerId);
        validateSubscription(userId, followerId);
        Subscription addingSubscription = Subscription
                .builder()
                .user(user)
                .follower(follower)
                .build();
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
                .findByFollower(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> getMyFollowers(long userId) {
        validateAndGetUser(userId);
        return subscriptionRepository
                .findByUser(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    private void validateSubscription(long userId, long followerId) {
        if (userId == followerId) {
            log.warn("User with userId: {} tried to follow to himself(followerId: {})", userId, followerId);
            throw new ConflictException("User with userId: " + userId + " tried to follow to himself(followerId: "
                    + followerId + ")");
        }
        Optional<Subscription> subscription = subscriptionRepository.findByUserAndFollower(userId, followerId);
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
        Optional<Subscription> subscription = subscriptionRepository.findByUserAndFollower(userId, followerId);
        if (subscription.isEmpty()) {
            log.warn("");
            throw new NotFoundException("");
        }
        return subscription.get();
    }
}
