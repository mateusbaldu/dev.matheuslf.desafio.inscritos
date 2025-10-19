package dev.matheuslf.desafio.inscritos.mappers;

import dev.matheuslf.desafio.inscritos.entities.Task;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMapping(TaskUpdateDto dto, @MappingTarget Task task);

    void createMapping(TaskCreateDto dto, @MappingTarget Task task);

    @Mapping(source = "project.id", target = "projectId")
    TaskResponseDto responseMapping(Task task);
}
