package ru.practicum.subscriptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventRespShort;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.users.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsController {

    private final SubscriptionsService subscriptionsService;

    @PostMapping("/{userId}/subscribe/{followerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribeToUser(@PathVariable(name = "userId") long userId,
                                           @PathVariable(name = "followerId") long followerId) {
        log.info("SubscriptionsController, subscribeToUser, userId: {}, followerId: {}", userId, followerId);
        return subscriptionsService.subscribeToUser(userId, followerId);

    }

    @DeleteMapping("/{userId}/subscribe/{followerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSubscribe(@PathVariable(name = "userId") long userId,
                                @PathVariable(name = "followerId") long followerId) {
        log.info("SubscriptionsController, cancelSubscribe, userId: {}, followerId: {}", userId, followerId);
        subscriptionsService.cancelSubscribe(userId, followerId);

    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersIFollow(@PathVariable(name = "userId") long userId) {
        log.info("SubscriptionsController, getUsersIFollow, userId: {}", userId);
        return subscriptionsService.getUsersIFollow(userId);
    }

    @GetMapping("/{userId}/followers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getMyFollowers(@PathVariable(name = "userId") long userId) {
        log.info("SubscriptionsController, getMyFollowers, userId: {}", userId);
        return subscriptionsService.getMyFollowers(userId);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventRespShort> getUsersEvents(@PathVariable(name = "userId") long userId,
                                               @RequestParam(value = "from", defaultValue = "0") int from,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("SubscriptionsController, getUsersEvents, userId: {}. From: {}, size: {}", userId, from, size);
        return subscriptionsService.getUsersEvents(userId, from, size);
    }
}
