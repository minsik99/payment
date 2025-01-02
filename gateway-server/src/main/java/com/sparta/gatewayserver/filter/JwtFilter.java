package com.sparta.gatewayserver.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

            // 토큰 존재 여부 확인
            if (token == null || !token.startsWith("Bearer ")) {
                throw new RuntimeException("Authorization header missing or invalid");
            }

            // JWT 토큰 검증 로직 추가
            String jwt = token.substring(7); // "Bearer " 제거
            try {
                // JWT 유효성 검증 (예: 서명 및 클레임 확인)
                Jwts.parserBuilder()
                    .setSigningKey("yourSecretKey".getBytes()) // 실제 비밀키 사용
                    .build()
                    .parseClaimsJws(jwt);
            } catch (Exception e) {
                throw new RuntimeException("Invalid JWT token: " + e.getMessage());
            }

            // 검증 성공 시 요청을 다음 필터로 전달
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 필터에 필요한 설정 값을 여기에 추가
    }
}
