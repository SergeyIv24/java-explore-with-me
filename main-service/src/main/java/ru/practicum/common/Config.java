package ru.practicum.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;
import ru.practicum.statistic.StatisticClient;

@Component
@RequiredArgsConstructor
public class Config {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${stats-server.url}")
    private String serverUri;

    @Bean
    public StatisticClient statisticClient() {
        return new StatisticClient(serverUri, restTemplateBuilder);
    }

    @Bean
    public String serverUrl() {
        return serverUri;
    }
}
