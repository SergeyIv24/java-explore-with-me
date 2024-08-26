package ru.practicum.compilations.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events_by_compilations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventsByCompilation {

    @EmbeddedId
    private CompositeKeyForEventByComp compositeKey;

}
