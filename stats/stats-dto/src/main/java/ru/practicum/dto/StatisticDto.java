package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StatisticDto {

    @NotBlank(message = "empty application name")
    private String app;

    @NotBlank(message = "empty uri")
    private String uri;

    @NotBlank(message = "empty ip")
    private String ip;

    @NotNull(message = "empty timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
