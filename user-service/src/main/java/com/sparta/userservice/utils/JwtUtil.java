package com.sparta.userservice.utils;

import com.sparta.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // Access Token과 Refresh Token의 유효 시간
    private final long accessTokenValidity = 3600000; // 1시간
    private final long refreshTokenValidity = 604800000; // 7일

    // Access Token 생성
    public String createAccessToken(String email, User.Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role.name());
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        log.info("유저 서비스 시크릿 키 : {}", secretKey);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 Claim 데이터 추출
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }
}
