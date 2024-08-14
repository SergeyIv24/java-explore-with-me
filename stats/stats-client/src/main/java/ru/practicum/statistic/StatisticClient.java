package ru.practicum.statistic;

import ru.practicum.GeneralConstance;
import ru.practicum.dto.StatisticDto;
import jakarta.annotation.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.BaseClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        String encodedStartData = encodeParameters(convertLocalDataTimeToString(start));
        String encodedEndData = encodeParameters(convertLocalDataTimeToString(end));

        Map<String, Object> parameters = new HashMap<>(Map.of("start", encodedStartData,
                "end", encodedEndData,
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

    private String encodeParameters(String parameter) {
        return URLEncoder.encode(parameter, StandardCharsets.UTF_8);
    }

    private String convertLocalDataTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(GeneralConstance.DATE_FORMATTER);
    }
}
