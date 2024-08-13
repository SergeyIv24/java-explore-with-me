package ru.practicum.statistic;

import dto.StatisticDto;
import jakarta.annotation.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.BaseClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticClient extends BaseClient {

    public StatisticClient(RestTemplateBuilder builder, String serverUrl) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           @Nullable List<String> uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of("start", start,
                "end", end,
                "unique", unique));
        if (uris != null) {
            parameters.put("uris", uris);
        }

        String apiStat = "/stat";
        return get(apiStat + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> addStat(StatisticDto statisticDto) {
        String apiHits = "/hit";
        return post(apiHits, statisticDto, null);
    }
}
