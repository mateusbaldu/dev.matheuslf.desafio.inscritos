package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginRequest;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginResponse;
import dev.matheuslf.desafio.inscritos.exceptions.InvalidLoginException;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.repositories.UserRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private LoginService loginService;

    private User user;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private String fakeTokenValue;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        JwtClaimsSet fakeClaims = JwtClaimsSet.builder()
                .issuer("desafio.api.test")
                .subject(uuid.toString())
                .expiresAt(Instant.now().plusSeconds(1800))
                .build();
        JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(fakeClaims);
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("Test", "none")
                .claims(claims -> claims.putAll(fakeClaims.getClaims()))
                .build();
        fakeTokenValue = fakeJwt.getTokenValue();
        user = new User(
                uuid,
                "testemail@email.com",
                "Test User",
                "password"
        );
        loginRequest = new LoginRequest(
                "testemail@email.com",
                "password"
        );
        loginResponse = new LoginResponse(
                fakeTokenValue,
                1800L
        );
    }

    @Nested
    class login {
        @Test
        @DisplayName("should return LoginResponse when everything is ok")
        void login_returnLoginResponse_whenEverythingIsOk() {
            doReturn(Optional.of(user)).when(userRepository).findByEmail("testemail@email.com");
            when(user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)).thenReturn(true);
            doReturn(fakeTokenValue).when(tokenService).generateToken(user);

            var output = loginService.login(loginRequest);

            assertNotNull(output);
            assertEquals(output, loginResponse);
            verify(userRepository, times(1)).findByEmail(anyString());
            verify(tokenService, times(1)).generateToken(any(User.class));
        }

        @Test
        @DisplayName("should throw Exception when user isnt found")
        void login_throwResourceNotFoundException_whenUserIsntFound() {
            doReturn(Optional.empty()).when(userRepository).findByEmail("testemail@email.com");

            assertThrows(ResourceNotFoundException.class, () -> loginService.login(loginRequest));
            verify(userRepository, times(1)).findByEmail(anyString());
        }

        @Test
        @DisplayName("should throw Exception when password is wrong on LoginRequest")
        void login_throwInvalidLoginException_whenUserIsntFound() {
            doReturn(Optional.of(user)).when(userRepository).findByEmail("testemail@email.com");
            when(user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)).thenReturn(false);

            assertThrows(InvalidLoginException.class, () -> loginService.login(loginRequest));

            verify(userRepository, times(1)).findByEmail(anyString());
        }
    }
}