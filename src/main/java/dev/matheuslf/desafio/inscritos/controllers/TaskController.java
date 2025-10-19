package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskUpdateDto;
import dev.matheuslf.desafio.inscritos.entities.enums.*;
import dev.matheuslf.desafio.inscritos.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/project-manager")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(dto));
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskResponseDto>> findTasks(@RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority, @RequestParam(required = false) Long projectId, Pageable pageable) {
        return ResponseEntity.ok(taskService.findTasks(status, priority, projectId, pageable));
    }

    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(@RequestBody Status status, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
