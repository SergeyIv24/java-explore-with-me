package ru.practicum.compilations.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events_by_compilations")
@Getter
@Setter
@AllArgsConstructor
public class EventsByCompilation {
    @EmbeddedId
    private CompositeKeyForEventByComp compositeKey;
}
