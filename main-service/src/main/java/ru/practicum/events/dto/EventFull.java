package ru.practicum.events.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFull {

    private Long id;

    @NotBlank(message = "empty annotation")
    private String annotation;

    private Category category;

    //private Long confirmedRequests;

    private LocalDateTime createdOn;

    @NotBlank(message = "empty description")
    private String description;

    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    private LocalDateTime eventDate;

    @NotNull
    private User initiator;

    @NotNull
    private Location location;

    @NotNull(message = "paid must be true or false")
    private Boolean paid;

    private Long participantLimit;

    @NotNull
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private String state;

    @NotBlank(message = "empty title")
    private String title;

    //private Long views;
}
