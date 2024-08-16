package ru.practicum.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class CategoryDto {

    private Integer id;

    @NotBlank(message = "empty category name")
    @Length(max = 50, min = 2)
    private String name;
}
