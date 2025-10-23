package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectUpdateDto;
import dev.matheuslf.desafio.inscritos.mappers.ProjectMapper;
import dev.matheuslf.desafio.inscritos.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private ProjectCreateDto projectCreate;
    private ProjectUpdateDto projectUpdate;
    private ProjectResponseDto projectResponse;
    private Project project;
    private Page<Project> pageResponse;

    @BeforeEach
    void setUp() {
        projectCreate = new ProjectCreateDto(
                "Project test",
                "Project test description",
                LocalDate.now(),
                LocalDate.MAX
        );
        project = new Project(
                1L,
                "Project test",
                "Project test description",
                LocalDate.now(),
                LocalDate.MAX
        );
        projectResponse = new ProjectResponseDto(
                1L,
                "Project test",
                "Project test description",
                LocalDate.now(),
                LocalDate.MAX
        );

        pageResponse = new PageImpl<>(List.of(project), PageRequest.of(0, 10), 1);

    }


    @Nested
    class create {
        @Test
        @DisplayName("Should return Project Response when everything is ok")
        void create_returnProjectResponse_whenEverythingIsOk() {
            doNothing().when(projectMapper).createMapping(eq(projectCreate), any(Project.class));
            doReturn(project).when(projectRepository).save(any(Project.class));
            doReturn(projectResponse).when(projectMapper).responseMapping(any(Project.class));

            var output = projectService.create(projectCreate);

            assertNotNull(output);
            assertEquals(projectResponse, output);
            verify(projectMapper, times(1)).createMapping(eq(projectCreate), any(Project.class));
            verify(projectRepository, times(1)).save(any(Project.class));
            verify(projectMapper, times(1)).responseMapping(any(Project.class));
        }
    }

    @Nested
    class listAll {
        @Test
        @DisplayName("Should return a Page of Project Response when everything is ok")
        void listAll_returnPageProjectResponse_whenEverythingIsOk() {
            doReturn(pageResponse).when(projectRepository).findAll(any(PageRequest.class));
            doReturn(projectResponse).when(projectMapper).responseMapping(any(Project.class));

            var output = projectService.listAll(Pageable.ofSize(10));

            assertNotNull(output);
            verify(projectMapper, times(1)).responseMapping(any(Project.class));
            verify(projectRepository, times(1)).findAll(any(PageRequest.class));
        }
    }
}