package ru.practicum.statistic.repository;

import ru.practicum.dto.StatisticResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistic.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT new ru.practicum.dto.StatisticResponse(st.app, st.uri, COUNT(st.ip)) " +
            "FROM Statistic AS st " +
            "WHERE st.uri IN ?1 " +
            "AND st.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<StatisticResponse> findByUriInAndStartBetween(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatisticResponse(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Statistic AS st " +
            "WHERE st.uri IN ?1 " +
            "AND st.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<StatisticResponse> findByUriInAndStartBetweenUniqueIp(List<String> uri, LocalDateTime start,
                                                               LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatisticResponse(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Statistic AS st " +
            "WHERE st.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<StatisticResponse> findStartBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatisticResponse(st.app, st.uri, COUNT(st.ip)) " +
            "FROM Statistic AS st " +
            "WHERE st.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC ")
    List<StatisticResponse> findStartBetweenUniqueIp(LocalDateTime start, LocalDateTime end);

}
