package ru.practicum.statistic.mappers;

import dto.StatisticDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ru.practicum.statistic.model.App;
import ru.practicum.statistic.model.Statistic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticMapper {

    public static Statistic mapToStatistic(StatisticDto statisticDto, App app) {
        return Statistic
                .builder()
                .app(app)
                .uri(statisticDto.getUri())
                .ip(statisticDto.getIp())
                .timestamp(statisticDto.getTimestamp())
                .build();
    }

    public static StatisticDto mapToStatisticDto(Statistic statistic) {
        return StatisticDto.builder()
                .app(statistic.getApp().getName())
                .uri(statistic.getUri())
                .ip(statistic.getIp())
                .timestamp(statistic.getTimestamp())
                .build();
    }

}
