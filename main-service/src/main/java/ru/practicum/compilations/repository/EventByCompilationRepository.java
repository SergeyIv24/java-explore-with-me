package ru.practicum.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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

    @Query(value = "SELECT * " +
            "FROM events_by_compilations " +
            "WHERE compilation_id IN (?1) ",
            nativeQuery = true)
    List<EventsByCompilation> findByCompilationIdIn(Collection<Integer> compilationId);
}
