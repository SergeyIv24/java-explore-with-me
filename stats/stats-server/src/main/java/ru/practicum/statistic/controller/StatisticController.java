package ru.practicum.statistic.controller;


import dto.StatisticDto;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.statistic.service.StatisticService;

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
    public void getStats(@PathParam("start") LocalDateTime start,
                         @PathParam("end") LocalDateTime end,
                         @PathParam("uris") List<String> uris,
                         @PathParam("unique") boolean unique) {

    }

}
