package dev.matheuslf.desafio.inscritos.entities.dtos.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotEmpty(message = "Email is required")
        String email,
        @NotEmpty(message = "Password is required")
        @Size(min = 8, max = 32, message = "Invalid length")
        String password
) {
}
