package ru.practicum.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.subscriptions.model.Subscription;
import ru.practicum.subscriptions.model.UserProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndFollowerId(long userId, long followerId);

    @Query(value = "SELECT u.* " +
            "FROM subscriptions AS s " +
            "INNER JOIN users AS u ON s.user_id = u.id " +
            "WHERE follower = ?1",
            nativeQuery = true)
    List<UserProjection> findByFollowerId(long followerId);

    @Query(value = "SELECT u.* " +
            "FROM subscriptions AS s " +
            "INNER JOIN users AS u ON s.follower = u.id " +
            "WHERE user_id = ?1",
            nativeQuery = true)
    List<UserProjection> findByUserId(long userId);
}
