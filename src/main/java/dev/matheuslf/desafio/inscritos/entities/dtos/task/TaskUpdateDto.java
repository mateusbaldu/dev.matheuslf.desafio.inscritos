package dev.matheuslf.desafio.inscritos.entities.dtos.task;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;

import java.time.LocalDate;

public record TaskUpdateDto(
        String title,
        String description,
        Priority priority,
        Status status,
        LocalDate dueDate,
        Long projectId
) {
}
