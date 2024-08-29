package ru.practicum.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.Errors.ClientException;
import ru.practicum.dto.StatisticResponse;
import ru.practicum.statistic.StatisticClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ConnectToStatServer {

    public static List<Long> getViews(LocalDateTime start, LocalDateTime end, String uris, boolean unique,
                                      StatisticClient statisticClient) {
        ResponseEntity<List<StatisticResponse>> response = statisticClient.getStats(start, end, uris, unique);


        if (response.getStatusCode().is4xxClientError()) {
            log.warn("Bad request. Status code is {}", response.getStatusCode());
            throw new ClientException("Bad request. Status code is: " + response.getStatusCode());
        }

        if (response.getStatusCode().is5xxServerError()) {
            log.warn("Internal server error statusCode is {}", response.getStatusCode());
            throw new ClientException("Internal server error statusCode is " + response.getStatusCode());
        }

        if (response.getBody() == null) {
            log.warn("Returned empty body");
            throw new ClientException("Returned empty body");
        }

        List<StatisticResponse> statisticResponses = response.getBody();

        return statisticResponses
                .stream()
                .map(StatisticResponse::getHits)
                .collect(Collectors.toList());
    }

    public static String prepareUris(List<Long> ids) {
        return ids
                .stream()
                .map((id) -> "event/" + id).collect(Collectors.joining());
    }
}
