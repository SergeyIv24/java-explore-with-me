package ru.practicum.statistic.service;

import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.statistic.exceptions.NotFoundException;
import ru.practicum.statistic.exceptions.ValidationException;
import ru.practicum.statistic.mappers.StatisticMapper;
import ru.practicum.statistic.model.App;
import ru.practicum.statistic.model.Statistic;
import ru.practicum.statistic.repository.AppRepository;
import ru.practicum.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImp implements StatisticService {

    private final StatisticRepository statisticRepository;
    private final AppRepository appRepository;

    @Override
    public StatisticDto addToStats(StatisticDto statisticDto) {
        App app = checkApp(statisticDto.getApp());
        Statistic statistic = StatisticMapper.mapToStatistic(statisticDto, app);
        return StatisticMapper.mapToStatisticDto(statisticRepository.save(statistic));
    }

    @Override
    public List<StatisticResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (unique) {
            if (uris == null) {
                log.info("Finding stats for unique IP and for all URI");
                return getStatsForAllEndpointsByUniqueIp(start, end);
            }
            log.info("Finding stats for unique IP and for List of URI");
            return getStatsByUniqueIp(start, end, uris);
        }

        if (uris == null) {
            log.info("Finding stats for all IP and for all URI");
            return getStatsForAllEndpointsByAllIp(start, end);
        }

        log.info("Finding stats for all IP and for List of URI");
        return getStatsByAllIp(start, end, uris);
    }

    private List<StatisticResponse> getStatsByUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return statisticRepository.findByUriInAndStartBetweenUniqueIp(uris, start, end);
    }

    private List<StatisticResponse> getStatsByAllIp(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return statisticRepository.findByUriInAndStartBetween(uris, start, end);
    }

    private List<StatisticResponse> getStatsForAllEndpointsByUniqueIp(LocalDateTime start, LocalDateTime end) {
        return statisticRepository.findStartBetweenUniqueIp(start, end);
    }

    private List<StatisticResponse> getStatsForAllEndpointsByAllIp(LocalDateTime start, LocalDateTime end) {
        return statisticRepository.findStartBetween(start, end);
    }

    private App checkApp(String appName) {
        Optional<App> app = appRepository.findByName(appName);
        if (app.isEmpty()) {
            log.warn("Adding app name is not existed. App name: {}", appName);
            throw new NotFoundException("Bad required app name");
        }
        return app.get();
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return;
        }
        if (start.isAfter(end)) {
            log.warn("Start is after end");
            throw new ValidationException("Start is after end");
        }

    }
}
