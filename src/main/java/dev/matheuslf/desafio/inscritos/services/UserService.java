package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserUpdateDto;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mappers.UserMapper;
import dev.matheuslf.desafio.inscritos.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto create(UserCreateDto dto) {
        User user = new User();
        userMapper.createMapping(dto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return userMapper.responseMapping(saved);
    }

    public UserResponseDto update(UserUpdateDto dto, String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateMapping(dto, user);
        User saved = userRepository.save(user);
        return userMapper.responseMapping(saved);
    }

    public void delete(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
