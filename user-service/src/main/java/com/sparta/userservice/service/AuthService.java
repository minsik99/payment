package com.sparta.userservice.service;

import com.sparta.userservice.dto.TokenResponse;
import com.sparta.userservice.entity.User;
import com.sparta.userservice.repository.UserRepository;
import com.sparta.userservice.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // Access Token 재발급
    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }
        String email = jwtUtil.getEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtUtil.createAccessToken(user.getEmail(), user.getRole());
    }

    // Google 사용자 인증 및 JWT 발급
    public TokenResponse authenticateGoogleUser(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // 새 사용자 생성
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setRole(User.Role.USER); // 기본 역할 설정
                    newUser.setBlock(false);
                    newUser.setSocialType("Google");
                    return userRepository.save(newUser);
                });

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        // TokenResponse 객체로 반환
        return new TokenResponse(accessToken, refreshToken);
    }
}