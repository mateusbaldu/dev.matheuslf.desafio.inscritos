package dev.matheuslf.desafio.inscritos.entities.dtos.project;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjectCreateDto(
        @NotNull
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
