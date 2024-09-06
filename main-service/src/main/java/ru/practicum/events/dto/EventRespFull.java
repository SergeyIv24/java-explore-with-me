package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class EventRespFull extends EventRespShort {
    //Other fields there are in parent

    @NotNull(message = "Empty createdOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotBlank(message = "empty description")
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    private Location location;

    @Min(value = 0, message = "negative participantLimit")
    private Integer participantLimit;

    @NotNull(message = "empty publishedOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private String state;


}
