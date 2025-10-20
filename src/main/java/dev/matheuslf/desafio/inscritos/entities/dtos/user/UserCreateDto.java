package dev.matheuslf.desafio.inscritos.entities.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateDto (
        String name,
        @NotEmpty(message = "Email is required")
        String email,
        @NotEmpty(message = "Password is required")
        @Size(min = 8, max = 32, message = "Invalid length")
        String password
){
}
