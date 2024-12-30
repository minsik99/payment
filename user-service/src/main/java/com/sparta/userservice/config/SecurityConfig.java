package com.sparta.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 무상태 세션 정책
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/actuator/**", "/eureka/**", "/auth/**").permitAll() // Actuator 및 Eureka 엔드포인트 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

    // 인증 실패 시 응답 설정
    static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication is required to access this resource.\"}");
            log.error("Unauthorized access error: {}", authException.getMessage());
        }
    }
}