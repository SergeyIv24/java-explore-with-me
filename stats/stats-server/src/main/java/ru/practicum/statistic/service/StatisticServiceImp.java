package ru.practicum.statistic.service;

import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.statistic.exceptions.NotFoundException;
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
    public List<StatisticDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return null;
    }

    private App checkApp(String appName) {
        Optional<App> app = appRepository.findByName(appName);
        if (app.isEmpty()) {
            log.warn("Adding app name is not existed. App name: {}", appName);
            throw new NotFoundException("Bad required app name");
        }
        return app.get();
    }
}
