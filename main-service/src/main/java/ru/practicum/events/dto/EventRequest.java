package ru.practicum.events.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;

@Data
@Builder

public class EventRequest {

    private Long id;

    @NotBlank(message = "empty annotation")
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank(message = "empty description")
    private String description;

    private Long initiator;

    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    private LocalDateTime eventDate;

    @NotNull(message = "Location must be existed")
    private Location location;

    @NotNull(message = "paid must be true or false")
    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "empty title")
    private String title;

    private LocalDateTime createdOn;

    private String state;

}
