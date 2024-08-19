package ru.practicum.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "empty annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "created_on")
    //todo valid date???
    private LocalDateTime createdOn;

    @NotBlank(message = "empty description")
    private String description;

    @Column(name = "event_date")
    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;

    //is not using @ElementCollection because Location is not simple type
    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @NotNull(message = "paid must be true or false")
    private Boolean paid;

    @Column(name = "participants_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    @FutureOrPresent(message = "publishedOn must not be in past")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    //todo valid not null???
    private Boolean requestModeration;

/*    @ManyToOne
    @JoinColumn(name = "state")
    private State state;*/

    private String state;

    @NotBlank(message = "empty title")
    private String title;
}
