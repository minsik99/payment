package com.sparta.userservice.controller;

import com.sparta.userservice.dto.TokenResponse;
import com.sparta.userservice.dto.GoogleUserInfo;
import com.sparta.userservice.entity.User;
import com.sparta.userservice.service.AuthService;
import com.sparta.userservice.service.GoogleOAuthService;
import com.sparta.userservice.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(GoogleOAuthService googleOAuthService, AuthService authService, JwtUtil jwtUtil) {
        this.googleOAuthService = googleOAuthService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // Google 로그인 URL 생성 및 반환
    @GetMapping("/google/login")
    public ResponseEntity<String> googleLogin() {
        String googleAuthUrl = googleOAuthService.getGoogleAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", googleAuthUrl).build();
    }

    // Google 인증 후 Callback 처리
    @GetMapping("/google/callback")
    public ResponseEntity<TokenResponse> googleCallback(@RequestParam String code) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(code);
            TokenResponse tokenResponse = authService.authenticateGoogleUser(
                    googleUserInfo.getEmail(),
                    googleUserInfo.getName()
            );
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Google OAuth Callback Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 엑세스 토큰 재발행
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Authorization 헤더에서 토큰 추출
            String refreshToken = authorizationHeader.replace("Bearer ", "");

            // Refresh Token 유효성 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                log.error("Invalid or expired Refresh Token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Refresh Token에서 사용자 이메일 추출
            String email = jwtUtil.getEmail(refreshToken);

            // 새 Access Token 발급
            String newAccessToken = jwtUtil.createAccessToken(email, User.Role.USER);
            log.info("New Access Token generated");

            // 새 토큰 반환
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        } catch (Exception e) {
            log.error("Error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Access Token이 유효하지 않습니다.");
        }

        return ResponseEntity.ok("로그아웃 완료");
    }
}