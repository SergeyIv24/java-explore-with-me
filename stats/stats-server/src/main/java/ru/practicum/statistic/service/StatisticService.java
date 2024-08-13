package ru.practicum.statistic.service;


import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    StatisticDto addToStats(StatisticDto statisticDto);

    List<StatisticResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
