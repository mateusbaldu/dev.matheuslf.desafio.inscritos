package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.configs.security.TokenConfig;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginRequest;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginResponse;
import dev.matheuslf.desafio.inscritos.exceptions.InvalidLoginException;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenConfig tokenConfig;
    private final UserRepository userRepository;

    public LoginService(BCryptPasswordEncoder bCryptPasswordEncoder, TokenConfig tokenConfig, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() ->
                new ResourceNotFoundException("User with email " + loginRequest.email() + " not found"));

        if (!user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new InvalidLoginException("Invalid password");
        }

        long expiresIn = 1800L;
        return new LoginResponse(tokenConfig.generateToken(user), expiresIn);
    }
}
