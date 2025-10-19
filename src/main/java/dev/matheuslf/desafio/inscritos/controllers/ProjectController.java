package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectResponseDto;
import dev.matheuslf.desafio.inscritos.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project-manager")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDto> create(@Valid @RequestBody ProjectCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(dto));
    }

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectResponseDto>> listAll(Pageable pageable) {
        return ResponseEntity.ok(projectService.listAll(pageable));
    }
}
