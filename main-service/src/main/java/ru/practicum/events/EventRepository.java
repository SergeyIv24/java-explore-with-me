package ru.practicum.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.dto.EventRespFull;
import ru.practicum.events.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByIdAndInitiatorId(long id, long initiatorId);

    List<Event> findByInitiatorId(long userId, Pageable pageable);

}
