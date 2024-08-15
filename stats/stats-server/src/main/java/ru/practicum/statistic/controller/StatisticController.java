package ru.practicum.statistic.controller;

import ru.practicum.GeneralConstants;
import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.statistic.service.StatisticService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticController {
    private final StatisticService statisticService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatisticDto addInStats(@Valid @RequestBody StatisticDto statisticDto) {
        log.info("StatisticController, addInStats, Request body app: {}, uri: {}, ip: {}, timestamp: {}",
                statisticDto.getApp(), statisticDto.getUri(), statisticDto.getIp(), statisticDto.getTimestamp());
        return statisticService.addToStats(statisticDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatisticResponse> getStats(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String start,
                                            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String end,
                                            @RequestParam(required = false, value = "uris") List<String> uris,
                                            @RequestParam(required = false, value = "unique") boolean unique) {
        log.info("Statistic Controller, getStats, parameters: start {}, end {}, uris {}, unique {}",
                start, end, uris, unique);
        LocalDateTime startDataTime = convertToLocalDataTime(decodeParameters(start));
        LocalDateTime endDataTime = convertToLocalDataTime(decodeParameters(end));
        return statisticService.getStats(startDataTime, endDataTime, uris, unique);
    }

    private String decodeParameters(String parameter) {
        return URLDecoder.decode(parameter, StandardCharsets.UTF_8);
    }

    private LocalDateTime convertToLocalDataTime(String dataTime) {
        return LocalDateTime.parse(dataTime, GeneralConstants.DATE_FORMATTER);
    }


}
