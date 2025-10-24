package dev.matheuslf.desafio.inscritos.generator;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import dev.matheuslf.desafio.inscritos.entities.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenGenerator {
    private final JwtEncoder jwtEncoder;

    public TokenGenerator(JwtEncoder jwtEncoder) {
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
