package ru.practicum.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
