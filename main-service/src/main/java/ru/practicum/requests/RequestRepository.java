package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.Requests;

import java.util.Collection;
import java.util.Set;

@Repository
public interface RequestRepository extends JpaRepository<Requests, Long> {

    Set<Requests> findByRequesterId(long requesterId);

    long countByEventId(long eventId);

}
