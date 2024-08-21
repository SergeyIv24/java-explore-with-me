package ru.practicum.requests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestsForConfirmation {

    @NotNull(message = "Empty requestsIds")
    private List<Long> requestIds;

    @NotBlank(message = "Empty status")
    private String status;

}
