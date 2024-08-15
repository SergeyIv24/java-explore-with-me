package ru.practicum.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    @Email(message = "email is not correct")
    private String email;
    @NotBlank(message = "empty name")
    private String name;
}
