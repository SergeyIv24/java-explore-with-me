package ru.practicum.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByIdAndInitiatorId(long id, long initiatorId);

    List<Event> findByInitiatorId(long userId, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "WHERE ((e.state IN (?1) OR ?1 IS NULL) " +
            "AND (e.category IN (?2) OR ?2 IS NULL) " +
            "AND (e.initiator IN (?3) OR ?3 IS NULL) " +
            "AND (e.event_date BETWEEN ?4 AND ?5)) ", nativeQuery = true)
    List<Event> findByConditionals(List<String> state, List<Integer> category, List<Long> initiator,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

}

