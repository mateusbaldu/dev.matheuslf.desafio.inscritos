
package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private Jwt fakeJwt;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        user = new User(
                uuid,
                "testemail@email.com",
                "Test User",
                "password"
        );

        JwtClaimsSet fakeClaims = JwtClaimsSet.builder()
                .issuer("desafio.api.test")
                .subject(uuid.toString())
                .expiresAt(Instant.now().plusSeconds(1800))
                .build();
        fakeJwt = Jwt.withTokenValue("fake-token")
                .header("Test", "none")
                .claims(claims -> claims.putAll(fakeClaims.getClaims()))
                .build();
    }

    @Nested
    class generateToken {
        @Test
        @DisplayName("Should return a token value successfully when everything is ok")
        void generateToken_returnTokenValue_WhenEverythingIsOk() {
            doReturn(fakeJwt).when(jwtEncoder).encode(any(JwtEncoderParameters.class));

            String output = tokenService.generateToken(user);

            assertNotNull(output);
            verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        }
    }
}