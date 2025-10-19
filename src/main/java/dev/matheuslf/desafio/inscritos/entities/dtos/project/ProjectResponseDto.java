package dev.matheuslf.desafio.inscritos.entities.dtos.project;

import java.time.LocalDate;

public record ProjectResponseDto(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
