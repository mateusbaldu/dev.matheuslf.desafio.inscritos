package dev.matheuslf.desafio.inscritos.services;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import dev.matheuslf.desafio.inscritos.entities.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {
        long expiresAt = 1800L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("desafio.api")
                .subject(user.getId().toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiresAt))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
