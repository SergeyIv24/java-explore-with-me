package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;

@Data
@Builder

public class EventRequest {

    private Long id;

    @NotBlank(message = "empty annotation")
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank(message = "empty description")
    @Length(min = 20, max = 7000)
    private String description;

    private Long initiator;

    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Location must be existed")
    private Location location;

    private Boolean paid;

    @Min(value = 0, message = "negative participantLimit")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "empty title")
    @Length(min = 3, max = 120)
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String state;

}
