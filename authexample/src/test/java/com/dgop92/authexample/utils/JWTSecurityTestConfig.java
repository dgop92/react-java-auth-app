package com.dgop92.authexample.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;

// Copy from https://stackoverflow.com/a/61790559

@TestConfiguration
public class JWTSecurityTestConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        // This anonymous class needs for the possibility of using SpyBean in test methods
        // Lambda cannot be a spy with spring @SpyBean annotation
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) {
                return jwt();
            }
        };
    }

    public Jwt jwt() {

        // This is a place to add general and maybe custom claims which should be available after parsing token in the live system
        Map<String, Object> claims = Map.of(
                "sub", "loasdasdiuauh",
                "name", "John Doe",
                "email", "jhondoe@example.com"
        );

        //This is an object that represents contents of jwt token after parsing
        return new Jwt(
                "a-dummy-token",
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                claims
        );
    }
}
