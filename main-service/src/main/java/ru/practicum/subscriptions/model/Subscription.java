package ru.practicum.subscriptions.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.users.model.User;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "user must be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower")
    @NotNull(message = "follower must be null")
    private User follower;


}
