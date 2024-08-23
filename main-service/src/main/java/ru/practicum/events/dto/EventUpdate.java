package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
public class EventUpdate {

    private Long id;

    @Length(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Length(min = 20)
    private String description;

    private Long initiator;

    @FutureOrPresent(message = "eventDate must be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @Min(value = 0, message = "negative participantLimit")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120)
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String stateAction;

}
