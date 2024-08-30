package ru.practicum.subscriptions.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.users.model.User;

@Data
@AllArgsConstructor
public class SubscriptionDto {

    private Long subscriptionId;

    @NotNull(message = "user must be null")
    private User user;

    @NotNull(message = "follower must be null")
    private User follower;
}
