package ru.practicum.events.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
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
public class EventRespFull {

    private Long id;

    @NotBlank(message = "empty annotation")
    private String annotation;

    @NotNull(message = "Category must be existed")
    private Category category;

    @Min(0)
    private Long confirmedRequests;

    @NotNull(message = "Empty createdOn")
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

    private Integer participantLimit;

    @NotNull(message = "empty publishedOn")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private String state;

    @NotBlank(message = "empty title")
    private String title;

    //private Long views;
}
