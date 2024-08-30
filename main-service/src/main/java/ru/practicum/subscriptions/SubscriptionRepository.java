package ru.practicum.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.subscriptions.model.Subscription;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserAndFollower(long user, long follower);

    List<User> findByFollower(long follower);

    List<User> findByUser(long user);

}
