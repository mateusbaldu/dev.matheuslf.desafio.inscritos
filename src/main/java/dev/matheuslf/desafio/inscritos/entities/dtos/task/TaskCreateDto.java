package dev.matheuslf.desafio.inscritos.entities.dtos.task;

import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record TaskCreateDto(
        @NotEmpty(message = "title is required")
        String title,
        String description,
        Priority priority,
        Status status,
        LocalDate dueDate,
        Long projectId
) {
}
