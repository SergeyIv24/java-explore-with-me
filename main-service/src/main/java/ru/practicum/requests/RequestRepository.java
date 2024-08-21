package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.Requests;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Requests, Long> {

    Collection<Requests> findByEventId(long eventId);

    List<Requests> findByRequesterId(long requesterId);

    int countByEventIdAndStatus(long eventId, String requestState);

    @Query(value = "select count(id) " +
            "from requests " +
            "where event IN ?1 " +
            "AND status LIKE ?2 " +
            "group by event ", nativeQuery = true)
    List<Long> countByEventIdInAndStatusGroupByEvent(List<Long> eventId, String requestState);

    List<Requests> findByIdInAndEventId(List<Long> id, long eventId);

}
