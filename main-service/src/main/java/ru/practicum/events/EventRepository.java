package ru.practicum.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.dto.EventFull;
import ru.practicum.events.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    EventFull findByIdAndInitiatorId(long id, long initiatorId);

}
