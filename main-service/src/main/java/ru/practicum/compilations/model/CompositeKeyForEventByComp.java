package ru.practicum.compilations.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeKeyForEventByComp implements Serializable {

    private Integer compilationId;
    private Long eventId;

}
