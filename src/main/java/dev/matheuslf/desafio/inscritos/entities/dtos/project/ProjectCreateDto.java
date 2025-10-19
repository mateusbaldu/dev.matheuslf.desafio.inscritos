package dev.matheuslf.desafio.inscritos.entities.dtos.project;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record ProjectCreateDto(
        @NotEmpty(message = "name is required")
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
