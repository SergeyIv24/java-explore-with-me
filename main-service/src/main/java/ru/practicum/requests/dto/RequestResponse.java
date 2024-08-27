package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestResponse {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
