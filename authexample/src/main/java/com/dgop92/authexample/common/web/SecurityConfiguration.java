package com.dgop92.authexample.common.web;

import com.dgop92.authexample.common.ratelimit.AnonymousThrottlingFilter;
import com.dgop92.authexample.common.ratelimit.GlobalRateLimitConfig;
import com.dgop92.authexample.path.ControllerPaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Value("${cors.allowed-origins}")
    private String allowedOriginsAsString;

    @Bean
    public AnonymousThrottlingFilter anonymousThrottlingFilter(GlobalRateLimitConfig globalRateLimitConfig) {
        return new AnonymousThrottlingFilter(globalRateLimitConfig.getPublicBucketConfigPerRoute());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AnonymousThrottlingFilter anonymousThrottlingFilter) throws Exception {

        List<String> allowedOrigins = List.of(allowedOriginsAsString.split(","));

        http.httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(anonymousThrottlingFilter, AuthorizationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(allowedOrigins);
                    corsConfig.setAllowedMethods(List.of("*"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    return corsConfig;
                }))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(String.format("%s/create-email-password", ControllerPaths.API_V1_USERS)).anonymous()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").anonymous()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }


}
