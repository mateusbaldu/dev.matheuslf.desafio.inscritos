package dev.matheuslf.desafio.inscritos.configs.mappers;

import dev.matheuslf.desafio.inscritos.entities.Project;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMapping(ProjectUpdateDto dto, @MappingTarget Project project);

    void createMapping(ProjectCreateDto dto, @MappingTarget Project project);

    ProjectResponseDto responseMapping(Project project);
}
