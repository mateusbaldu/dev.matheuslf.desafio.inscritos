package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.mappers.TaskMapper;
import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        Task task = new Task();
        taskMapper.createMapping(dto, task);
        Task saved = taskRepository.save(task);
        return taskMapper.responseMapping(saved);
    }

    public Page<TaskResponseDto> findTasks(Status status, Priority priority, Long projectId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByFilters(status, priority, projectId, pageable);
        return tasks.map(taskMapper::responseMapping);
    }

    public TaskResponseDto updateStatus(Long id, String status) {
        Task t = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        t.setStatus(Status.valueOf(status.toUpperCase()));
        Task saved = taskRepository.save(t);
        return taskMapper.responseMapping(saved);
    }
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
