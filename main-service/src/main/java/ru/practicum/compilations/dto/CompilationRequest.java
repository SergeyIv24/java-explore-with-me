package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
public class CompilationRequest {

    private Integer id;

    //@NotBlank(message = "Empty title")
    @Length(max = 50)
    private String title;

    private Boolean pinned;

    //@NotNull(message = "Events list must be existed")
    private List<Long> events;
}
