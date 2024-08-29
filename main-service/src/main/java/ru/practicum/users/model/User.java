package ru.practicum.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not correct")
    @NotBlank(message = "Empty email")
    @Length(min = 6, max = 254)
    private String email;

    @NotBlank(message = "empty name")
    @Length(min = 2, max = 250)
    private String name;
}
