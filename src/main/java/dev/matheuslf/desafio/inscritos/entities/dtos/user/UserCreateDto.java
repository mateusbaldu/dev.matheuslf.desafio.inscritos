package dev.matheuslf.desafio.inscritos.entities.dtos.user;

import jakarta.validation.constraints.NotEmpty;

public record UserCreateDto (
        String name,
        @NotEmpty(message = "Email is required")
        String email,
        @NotEmpty(message = "Password is required")
        String password
){
}
