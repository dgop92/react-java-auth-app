package com.dgop92.authexample.utils;

import com.dgop92.authexample.common.ratelimit.GlobalRateLimitConfig;
import com.dgop92.authexample.common.ratelimit.RouteRateLimitConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Copy from https://stackoverflow.com/a/61790559

@TestConfiguration
public class SecurityTestConfig {

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

    @Bean
    public GlobalRateLimitConfig globalRateLimitConfig() {
        // Empty maps make all endpoints free of rate limits
        return new GlobalRateLimitConfig() {
            @Override
            public Map<String, RouteRateLimitConfig> getPrivateBucketConfigPerRoute() {
                return new ConcurrentHashMap<>();
            }

            @Override
            public Map<String, RouteRateLimitConfig> getPublicBucketConfigPerRoute() {
                return new ConcurrentHashMap<>();
            }
        };
    }
}
