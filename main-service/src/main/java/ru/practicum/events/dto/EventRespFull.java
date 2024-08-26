package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class EventRespFull {

    private Long id;

    @NotBlank(message = "empty annotation")
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Category must be existed")
    private Category category;

    @Min(0)
    private Long confirmedRequests;

    @NotNull(message = "Empty createdOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotBlank(message = "empty description")
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private User initiator;

    @NotNull
    private Location location;

    @NotNull(message = "paid must be true or false")
    private Boolean paid;

    @Min(value = 0, message = "negative participantLimit")
    private Integer participantLimit;

    @NotNull(message = "empty publishedOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private String state;

    @NotBlank(message = "empty title")
    @Length(min = 3, max = 120)
    private String title;

    private Long views;
}
