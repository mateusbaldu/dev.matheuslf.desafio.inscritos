package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.mappers.ProjectMapper;
import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectResponseDto;
import dev.matheuslf.desafio.inscritos.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectResponseDto create(ProjectCreateDto dto) {
        Project p = new Project();
        projectMapper.createMapping(dto, p);
        Project saved = projectRepository.save(p);
        return projectMapper.responseMapping(saved);
    }

    public Page<ProjectResponseDto> listAll(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(projectMapper::responseMapping);
    }
}
