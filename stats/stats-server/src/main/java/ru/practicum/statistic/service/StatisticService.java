package ru.practicum.statistic.service;


import dto.StatisticDto;
import dto.StatisticResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    StatisticDto addToStats(StatisticDto statisticDto);

    List<StatisticResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
