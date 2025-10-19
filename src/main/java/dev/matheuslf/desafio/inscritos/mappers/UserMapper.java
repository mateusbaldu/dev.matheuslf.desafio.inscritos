package dev.matheuslf.desafio.inscritos.mappers;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMapping(UserUpdateDto dto, @MappingTarget User user);

    void createMapping(UserCreateDto dto, @MappingTarget User user);
    UserResponseDto responseMapping(User user);
}
