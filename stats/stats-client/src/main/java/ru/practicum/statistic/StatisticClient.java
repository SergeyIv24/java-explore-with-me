package ru.practicum.statistic;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.practicum.GeneralConstants;
import ru.practicum.dto.StatisticDto;
import jakarta.annotation.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.BaseClient;
import ru.practicum.dto.StatisticResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticClient extends BaseClient {

    public StatisticClient(String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<List<StatisticResponse>> getStats(LocalDateTime start, LocalDateTime end,
                                                      @Nullable String uris, boolean unique) {

        String encodedStartData = encodeParameters(convertLocalDataTimeToString(start));
        String encodedEndData = encodeParameters(convertLocalDataTimeToString(end));

        Map<String, Object> parameters = new HashMap<>(Map.of("start", encodedStartData,
                "end", encodedEndData,
                "unique", unique));

        if (uris != null) {
            parameters.put("uris", uris);
        }

        return getList("/stats" + "?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters,
                new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Object> addStat(StatisticDto statisticDto) {
        return post("/hit", statisticDto, null);
    }

    private String encodeParameters(String parameter) {
        return URLEncoder.encode(parameter, StandardCharsets.UTF_8);
    }

    private String convertLocalDataTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(GeneralConstants.DATE_FORMATTER);
    }
}
