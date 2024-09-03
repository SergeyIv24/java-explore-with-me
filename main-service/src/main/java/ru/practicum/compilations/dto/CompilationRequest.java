package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompilationRequest extends CompilationUpdate {

    @NotBlank(message = "Empty title")
    @Length(max = 50)
    private String title;
}