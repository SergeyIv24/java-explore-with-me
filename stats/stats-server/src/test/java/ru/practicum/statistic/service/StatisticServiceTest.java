package ru.practicum.statistic.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.GeneralConstants;
import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticResponse;

import ru.practicum.statistic.model.App;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatisticServiceTest {

    private final EntityManager em;
    private final StatisticService statisticService;

    private static StatisticDto statEvent1Ip1;
    private static StatisticDto statEvent2Ip1;
    private static StatisticDto statEvent6Ip2;
    private static StatisticDto statEventIp2;
    private static App mainApp;
    private static LocalDateTime start = LocalDateTime.parse("2000-01-01 11:11:11", GeneralConstants.DATE_FORMATTER);
    private static LocalDateTime end = LocalDateTime.parse("3000-01-01 11:11:11", GeneralConstants.DATE_FORMATTER);

    @BeforeAll
    static void setup() {
        mainApp = new App();
        mainApp.setId(1L);
        mainApp.setName("ewm-main-service");

        statEvent1Ip1 = StatisticDto
                .builder()
                .uri("event/1")
                .ip("1")
                .app(mainApp.getName())
                .timestamp((LocalDateTime.parse("2023-10-06 22:00:23", GeneralConstants.DATE_FORMATTER)))
                .build();

        statEvent2Ip1 = StatisticDto
                .builder()
                .uri("event/2")
                .ip("1")
                .app(mainApp.getName())
                .timestamp((LocalDateTime.parse("2023-11-06 20:00:23", GeneralConstants.DATE_FORMATTER)))
                .build();

        statEvent6Ip2 = StatisticDto
                .builder()
                .uri("event/1")
                .ip("2")
                .app(mainApp.getName())
                .timestamp((LocalDateTime.parse("2022-11-06 22:00:23", GeneralConstants.DATE_FORMATTER)))
                .build();

        statEventIp2 = StatisticDto
                .builder()
                .uri("event/")
                .ip("2")
                .app(mainApp.getName())
                .timestamp((LocalDateTime.parse("2021-10-06 22:00:23", GeneralConstants.DATE_FORMATTER)))
                .build();
    }

    @BeforeEach
    void addStats() {
        statisticService.addToStats(statEvent1Ip1);
        statisticService.addToStats(statEvent2Ip1);
        statisticService.addToStats(statEvent6Ip2);
        statisticService.addToStats(statEventIp2);
    }



    @Test
    void shouldGetStatForAllEndPointsAndAllIp() {

        List<StatisticResponse> stats = statisticService.getStats(start, end, null, false);

        assertThat(stats.size(), equalTo(3));
        assertThat(stats.get(0).getUri(), equalTo(statEvent1Ip1.getUri()));
        assertThat(stats.get(0).getHits(), equalTo(2L));

        assertThat(stats.get(1).getUri(), equalTo(statEventIp2.getUri()));
        assertThat(stats.get(1).getHits(), equalTo(1L));

        assertThat(stats.get(2).getUri(), equalTo(statEvent2Ip1.getUri()));
        assertThat(stats.get(2).getHits(), equalTo(1L));

    }

    @Test
    void shouldGetStatForAllEndPointsAndUniqueIp() {

        List<StatisticResponse> stats = statisticService.getStats(start, end, null, true);

        assertThat(stats.size(), equalTo(3));
        assertThat(stats.get(0).getUri(), equalTo(statEvent1Ip1.getUri()));
        assertThat(stats.get(0).getHits(), equalTo(2L));

        assertThat(stats.get(1).getUri(), equalTo(statEventIp2.getUri()));
        assertThat(stats.get(1).getHits(), equalTo(1L));

        assertThat(stats.get(2).getUri(), equalTo(statEvent2Ip1.getUri()));
        assertThat(stats.get(2).getHits(), equalTo(1L));
    }

    @Test
    void shouldGetStatForEndPointsListAndAllIp() {

        List<StatisticResponse> stats = statisticService.getStats(start, end, List.of("event/1"), false);

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getUri(), equalTo(statEvent1Ip1.getUri()));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void shouldGetStatForEndPointsListAndUniqueIp() {
        List<StatisticResponse> stats = statisticService.getStats(start, end, List.of("event/1", "events/2", "event/"), true);

        assertThat(stats.size(), equalTo(2));
        assertThat(stats.get(0).getUri(), equalTo(statEvent1Ip1.getUri()));
        assertThat(stats.get(0).getHits(), equalTo(2L));

        assertThat(stats.get(1).getUri(), equalTo(statEventIp2.getUri()));
        assertThat(stats.get(1).getHits(), equalTo(1L));
    }

}
