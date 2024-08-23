package ru.practicum.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
    @Length(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @NotBlank(message = "empty description")
    @Length(min = 20)
    private String description;

    @Column(name = "event_date")
    @NotNull(message = "event date must be existed")
    @FutureOrPresent(message = "eventDate must be in future")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @NotNull(message = "paid must be true or false")
    private Boolean paid;

    @Column(name = "participants_limit")
    @Min(value = 0, message = "negative participantLimit")
    private Integer participantLimit;

    @Column(name = "published_on")
    @FutureOrPresent(message = "publishedOn must not be in past")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    @NotNull(message = "requestModeration must be true or false")
    private Boolean requestModeration;

    private String state;

    @NotBlank(message = "empty title")
    @Length(min = 3, max = 120)
    private String title;
}