package dev.matheuslf.desafio.inscritos.entities.dtos.project;

import java.time.LocalDate;

public record ProjectUpdateDto(
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
