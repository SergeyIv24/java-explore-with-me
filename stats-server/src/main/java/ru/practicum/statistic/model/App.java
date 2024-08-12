
package ru.practicum.statistic.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "apps")
@Data
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long id;

    @Column(name = "app_name")
    private String name;
}

