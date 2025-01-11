package com.sparta.gatewayserver.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {
    @Value("${jwt.secret}")
    private String secretKey;

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 내부 통신 경로 (배치 작업)
            if (path.startsWith("/video-service/internal")) {
                log.info("배치 작업을 위해 jwt 검증 안함");
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

            // 토큰 존재 여부 확인
            if (token == null || !token.startsWith("Bearer ")) {
                throw new RuntimeException("Authorization header missing or invalid");
            }

            // JWT 토큰 검증 및 클레임 추출
            String jwt = token.substring(7);
            Claims claims;
            try {
                claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey.getBytes())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
            } catch (Exception e) {
                throw new RuntimeException("Invalid JWT token: " + e.getMessage());
            }

            // 클레임에서 이메일과 역할 추출
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // 클레임 데이터를 요청 헤더에 추가
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("email", email)
                    .header("role", role)
                    .build();

            // 수정된 요청을 다음 필터로 전달
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    public static class Config {
        // 필터에 필요한 설정 값을 여기에 추가
    }
}