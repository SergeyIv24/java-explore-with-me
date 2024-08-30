package ru.practicum.subscriptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.subscriptions.dto.SubscriptionDto;
import ru.practicum.subscriptions.model.Subscription;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {

    public static SubscriptionDto mapToSubscriptionDto(Subscription subscription) {
        return new SubscriptionDto(subscription.getSubscriptionId(),
                subscription.getUser(), subscription.getFollower());
    }

    public static Subscription mapToSubscription(SubscriptionDto subscriptionDto) {
        return new Subscription(subscriptionDto.getSubscriptionId(),
                subscriptionDto.getUser(), subscriptionDto.getFollower());
    }

}
