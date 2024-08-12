package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statistic.dto.StatisticDto;
import ru.practicum.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImp implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public StatisticDto addToStats(StatisticDto statisticDto) {
        return null;
    }

    @Override
    public List<StatisticDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return null;
    }
}
