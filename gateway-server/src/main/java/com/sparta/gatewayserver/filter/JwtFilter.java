package com.sparta.gatewayserver.filter;

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

            if (token == null || !token.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid Token");
            }

            // JWT 검증 로직 추가 (예: 유효성 검사 및 클레임 파싱)
            String jwt = token.substring(7);
            // 검증 성공 시, 요청을 다음 필터로 전달
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 필터에 필요한 설정 값을 여기에 추가
    }
}
