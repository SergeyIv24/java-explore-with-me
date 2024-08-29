package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationRequest {

    private Integer id;

    @NotBlank(message = "Empty title")
    @Length(max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
