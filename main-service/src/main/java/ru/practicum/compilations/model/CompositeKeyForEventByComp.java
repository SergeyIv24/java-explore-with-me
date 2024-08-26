package ru.practicum.compilations.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompositeKeyForEventByComp implements Serializable {

    private Integer compilationId;
    private Long eventId;

}
