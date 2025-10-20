package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginRequest;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginResponse;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserUpdateDto;
import dev.matheuslf.desafio.inscritos.services.LoginService;
import dev.matheuslf.desafio.inscritos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project-manager")
public class UserController {
    private final UserService userService;
    private final LoginService loginService;

    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @PostMapping("/users/new")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userCreateDto));
    }

    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserUpdateDto userUpdateDto, JwtAuthenticationToken token) {
        return ResponseEntity.ok(userService.update(userUpdateDto, token.getName()));
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> delete(JwtAuthenticationToken token) {
        userService.delete(token.getName());
        return ResponseEntity.noContent().build();
    }
}
