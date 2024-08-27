package ru.practicum.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilations.dto.EventByCompId;
import ru.practicum.compilations.model.CompositeKeyForEventByComp;
import ru.practicum.compilations.model.EventsByCompilation;

import java.util.Collection;
import java.util.List;

@Repository
public interface EventByCompilationRepository extends JpaRepository<EventsByCompilation, CompositeKeyForEventByComp> {

    @Query(value = "SELECT * " +
            "FROM events_by_compilations " +
            "WHERE compilation_id = ?1 ",
            nativeQuery = true)
    List<Long> findByCompilationId(int compilationId);

    @Query(value = "select compilation_id, e.* " +
            "from events_by_compilations AS ebc " +
            "INNER JOIN events AS e on ebc.event_id = e.id " +
            "where compilation_id IN (?1) ",
            nativeQuery = true)
    List<EventByCompId> findEventsByCompIdIn(Collection<Integer> compId);

    @Query(value = "delete from events_by_compilations " +
            "where compilation_id = ?1 ",
            nativeQuery = true)
    void deleteByCompilationId(int compilationId);


}
