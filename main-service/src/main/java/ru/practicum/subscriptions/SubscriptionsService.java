package ru.practicum.subscriptions;

import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface SubscriptionsService {

    SubscriptionDto subscribeToUser(long userId, long followerId);

    void cancelSubscribe(long userId, long followerId);

    List<UserDto> getUsersIFollow(long userId);

    List<UserDto> getMyFollowers(long userId);



}
