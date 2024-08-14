package ru.practicum.statistic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.GeneralConstance;
import ru.practicum.dto.StatisticDto;
import ru.practicum.dto.StatisticResponse;
import ru.practicum.statistic.model.App;
import ru.practicum.statistic.service.StatisticService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StatisticController.class)
public class StatisticControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StatisticService statisticService;

    @Autowired
    private MockMvc mvc;

    private static StatisticDto addingStatisticForEvent1;
    private static StatisticResponse statsForEvent1;
    private static App mainApp;

    @BeforeAll
    static void setup() {

        mainApp = new App();
        mainApp.setId(1L);
        mainApp.setName("ewm-main-service");

        addingStatisticForEvent1 = StatisticDto.builder()
                .app(mainApp.getName())
                .uri("/events/1")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.parse("2023-10-06 22:00:23", GeneralConstance.DATE_FORMATTER))
                .build();

        statsForEvent1 = new StatisticResponse(mainApp, "/events/1", 2L);
    }

    @Test
    void shouldAddStat() throws Exception {

        when(statisticService.addToStats(addingStatisticForEvent1))
                .thenReturn(addingStatisticForEvent1);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(addingStatisticForEvent1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.app", is(addingStatisticForEvent1.getApp()), String.class))
                .andExpect(jsonPath("$.uri", is(addingStatisticForEvent1.getUri()), String.class))
                .andExpect(jsonPath("$.ip", is(addingStatisticForEvent1.getIp()), String.class));
    }

    @Test
    void shouldReturnStatWithoutList() throws Exception {

        when(statisticService.getStats(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(statsForEvent1));

        App app = (App) statsForEvent1.getApp();
        String appName = app.getName();

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&unique=false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].uri", is(statsForEvent1.getUri()), String.class))
                .andExpect(jsonPath("$.[0].app.name", is(appName), String.class))
                .andExpect(jsonPath("$.[0].hits", is(statsForEvent1.getHits()), Long.class));
    }

    @Test
    void shouldReturnStatWithListAndUnique() throws Exception {
        when(statisticService.getStats(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(statsForEvent1));

        App app = (App) statsForEvent1.getApp();
        String appName = app.getName();

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris=[event/1]&unique=false")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].uri", is(statsForEvent1.getUri()), String.class))
                .andExpect(jsonPath("$.[0].app.name", is(appName), String.class))
                .andExpect(jsonPath("$.[0].hits", is(statsForEvent1.getHits()), Long.class));
    }

    @Test
    void shouldReturnStatWithoutUniqueParameter() throws Exception {

        when(statisticService.getStats(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(statsForEvent1));

        App app = (App) statsForEvent1.getApp();
        String appName = app.getName();

        mvc.perform(get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].uri", is(statsForEvent1.getUri()), String.class))
                .andExpect(jsonPath("$.[0].app.name", is(appName), String.class))
                .andExpect(jsonPath("$.[0].hits", is(statsForEvent1.getHits()), Long.class));
    }
}
