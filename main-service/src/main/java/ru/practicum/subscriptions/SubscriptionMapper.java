package ru.practicum.subscriptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.subscriptions.model.Subscription;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {

    public static SubscriptionDto mapToSubscriptionDto(Subscription subscription) {
        return SubscriptionDto
                .builder()
                .subscriptionId(subscription.getSubscriptionId())
                .user(subscription.getUser())
                .follower(subscription.getFollower())
                .build();
    }

    public static Subscription mapToSubscription(SubscriptionDto subscriptionDto) {
        return Subscription
                .builder()
                .subscriptionId(subscriptionDto.getSubscriptionId())
                .user(subscriptionDto.getUser())
                .follower(subscriptionDto.getFollower())
                .build();
    }

}
