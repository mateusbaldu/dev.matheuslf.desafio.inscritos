package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskUpdateDto;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mappers.TaskMapper;
import dev.matheuslf.desafio.inscritos.repositories.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repositories.TaskRepository;
import org.hibernate.annotations.DialectOverride;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Project project;
    private TaskResponseDto taskResponse;
    private TaskCreateDto taskCreate;
    private Page<Task> pageResponse;

    @BeforeEach
    void setUp() {
        project = new Project(
                1L,
                "Project test",
                "Project test description",
                LocalDate.now(),
                LocalDate.MAX
        );
        task = new Task(
                1L,
                "Test task",
                "Test task description",
                Priority.HIGH,
                Status.TODO,
                LocalDate.MAX,
                project
        );
        taskResponse = new TaskResponseDto(
                1L,
                "Test task",
                "Test task description",
                Priority.HIGH,
                Status.TODO,
                LocalDate.MAX,
                project.getId()
        );
        taskCreate = new TaskCreateDto(
                "Test task",
                "Test task description",
                Priority.HIGH,
                Status.TODO,
                LocalDate.MAX,
                project.getId()
        );
        pageResponse = new PageImpl<>(List.of(task), PageRequest.of(0, 10), 1);
    }

    @Nested
    class create {
        @Test
        @DisplayName("Should return Task Response when everything is ok")
        void create_returnTaskResponse_whenEverythingIsOk() {
            doReturn(Optional.of(project)).when(projectRepository).findById(anyLong());
            doNothing().when(taskMapper).createMapping(any(TaskCreateDto.class), any(Task.class));
            doReturn(task).when(taskRepository).save(any(Task.class));
            doReturn(taskResponse).when(taskMapper).responseMapping(any(Task.class));

            var output = taskService.create(taskCreate);

            assertNotNull(output);
            assertEquals(taskResponse, output);
            verify(projectRepository, times(1)).findById(1L);
            verify(taskMapper, times(1)).createMapping(eq(taskCreate), any(Task.class));
            verify(taskRepository, times(1)).save(any(Task.class));
            verify(taskMapper, times(1)).responseMapping(task);
        }

        @Test
        @DisplayName("Should throw a Exception when the Project isn't found")
        void create_throwResourceNotFoundException_whenProjectIsNotFound() {
            doReturn(Optional.empty()).when(projectRepository).findById(anyLong());

            assertThrows(ResourceNotFoundException.class, () -> taskService.create(taskCreate));
        }
    }

    @Nested
    class findTasks {
        @Test
        @DisplayName("Should return Page of Task Response when everything is ok")
        void findTasks_returnPageTaskResponse_whenEverythingIsOk() {
            doReturn(pageResponse).when(taskRepository).findByFilters(any(Status.class), any(Priority.class), anyLong(), any());
            doReturn(taskResponse).when(taskMapper).responseMapping(any(Task.class));

            var output = taskService.findTasks(Status.TODO, Priority.HIGH,1L, Pageable.ofSize(10));

            assertNotNull(output);
            assertEquals(taskResponse, output.getContent().getFirst());
            verify(taskRepository, times(1)).findByFilters(eq(Status.TODO), eq(Priority.HIGH), eq(1L), any());
            verify(taskMapper, times(1)).responseMapping(task);
        }
    }

    @Nested
    class updateStatus {
        @Test
        @DisplayName("Should return Task Response when everything is ok")
        void updateStatus_returnTaskResponse_whenEverythingIsOk() {
            task.setStatus(Status.DOING);
            taskResponse = new TaskResponseDto(
                    1L,
                    "Test task",
                    "Test task description",
                    Priority.HIGH,
                    Status.DOING,
                    LocalDate.MAX,
                    project.getId()
            );

            doReturn(Optional.of(task)).when(taskRepository).findById(anyLong());
            doReturn(task).when(taskRepository).save(any(Task.class));
            doReturn(taskResponse).when(taskMapper).responseMapping(any(Task.class));

            var output = taskService.updateStatus(1L, Status.DOING);

            assertNotNull(output);
            assertEquals(taskResponse, output);
            verify(taskRepository, times(1)).findById(1L);
            verify(taskRepository, times(1)).save(any(Task.class));
            verify(taskMapper, times(1)).responseMapping(task);
        }

        @Test
        @DisplayName("should throw Exception when Task isn't found")
        void updateStatus_throwResourceNotFoundException_whenTaskIsNotFound() {
            doReturn(Optional.empty()).when(taskRepository).findById(anyLong());

            assertThrows(ResourceNotFoundException.class, () -> taskService.updateStatus(1L, Status.DOING));
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("should return void when everything is ok")
        void delete_returnVoid_whenEverythingIsOk() {
            doReturn(true).when(taskRepository).existsById(anyLong());
            doNothing().when(taskRepository).deleteById(anyLong());

            taskService.delete(1L);

            verify(taskRepository, times(1)).existsById(1L);
            verify(taskRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw Exception when Task isn't found")
        void delete_throwResourceNotFoundException_whenTaskIsNotFound() {
            doReturn(false).when(taskRepository).existsById(anyLong());

            assertThrows(ResourceNotFoundException.class, () -> taskService.delete(1L));
        }
    }
}