package dev.matheuslf.desafio.inscritos.entities.dtos.user;

import jakarta.validation.constraints.NotNull;

public record UserCreateDto (
        String name,
        @NotNull(message = "Email can't be null")
        String email,
        @NotNull(message = "Password can't be null")
        String password
){
}
