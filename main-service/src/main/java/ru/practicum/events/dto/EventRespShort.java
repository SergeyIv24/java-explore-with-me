package ru.practicum.events.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.model.Category;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class EventRespShort {

    private Long id;

    @NotBlank(message = "empty annotation")
    private String annotation;

    @NotNull(message = "Category must be existed")
    private Category category;

    @Min(0)
    private Long confirmedRequests;

    @NotNull(message = "event date must be existed")
    private LocalDateTime eventDate;

    @NotNull
    private User initiator;
    private Boolean paid;

    @NotBlank(message = "empty title")
    private String title;

    @Min(0)
    private Long views;
}
