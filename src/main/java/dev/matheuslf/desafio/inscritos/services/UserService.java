package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
