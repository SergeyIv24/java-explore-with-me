package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.dto.EventRespShort;

import java.util.List;

@Data
@Builder
public class CompilationResponse {

    private Integer id;

    @NotBlank(message = "Empty title")
    @Length(max = 50)
    private String title;

    @NotNull(message = "Pinned must not be null")
    private Boolean pinned;

    //@NotNull(message = "Events list must be existed")
    private List<EventRespShort> events;

}
