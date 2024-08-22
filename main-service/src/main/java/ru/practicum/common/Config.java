package ru.practicum.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;
import ru.practicum.statistic.StatisticClient;

@Component
public class Config {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${stats-server.url}")
    private String serverUri;

    @Bean
    public StatisticClient StatisticClient() {
        return new StatisticClient(serverUri, restTemplateBuilder);
    }

    @Bean
    public String serverUrl() {
        return serverUri;
    }
}
