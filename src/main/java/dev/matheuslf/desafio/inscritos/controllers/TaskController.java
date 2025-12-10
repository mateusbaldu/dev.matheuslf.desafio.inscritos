package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.dtos.task.StatusUpdateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskUpdateDto;
import dev.matheuslf.desafio.inscritos.entities.enums.*;
import dev.matheuslf.desafio.inscritos.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task", description = "Endpoints for task management")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a task and return a task response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "404", description = "Project with id not found")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(dto));
    }

    @Operation(summary = "Find tasks based on query params like status, priority or projectId")
    @ApiResponse(responseCode = "200", description = "Tasks found successfully")
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> findTasks(@RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority, @RequestParam(required = false) Long projectId, Pageable pageable) {
        return ResponseEntity.ok(taskService.findTasks(status, priority, projectId, pageable));
    }

    @Operation(summary = "Update a status of a task and return task response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task with id not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(@RequestBody StatusUpdateDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateStatus(id, dto.status()));
    }

    @Operation(summary = "Delete a task and return a no content object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
