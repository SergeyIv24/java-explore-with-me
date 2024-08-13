package ru.practicum;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate restTemplate;

    protected ResponseEntity<Object> get(String uri, @Nullable Map<String, Object> parameters) {
        return sendRequest(uri, HttpMethod.GET, null, parameters);
    }

    protected <T> ResponseEntity<Object> post(String uri, @Nullable T body,
                                              @Nullable Map<String, Object> parameters) {
        return sendRequest(uri, HttpMethod.POST, body, parameters);
    }

    private <T> ResponseEntity<Object> sendRequest(String uri, HttpMethod method,
                                                   @Nullable T body,
                                                   @Nullable Map<String, Object> parameters) {

        HttpEntity<T> request = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> response;

        if (parameters == null) {
            response = restTemplate.exchange(uri, method, request, Object.class);
        } else {
            response = restTemplate.exchange(uri, method, request, Object.class, parameters);
        }
        return response;
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
