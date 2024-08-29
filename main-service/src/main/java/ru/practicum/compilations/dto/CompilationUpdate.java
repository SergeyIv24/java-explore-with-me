package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationUpdate {

    private Integer id;

    @Length(max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;

}
