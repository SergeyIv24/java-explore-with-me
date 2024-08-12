package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    StatisticDto addToStats(StatisticDto statisticDto);

    List<StatisticDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
