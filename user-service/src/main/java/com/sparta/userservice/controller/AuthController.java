package com.sparta.userservice.controller;

import com.sparta.userservice.dto.TokenResponse;
import com.sparta.userservice.dto.GoogleUserInfo;
import com.sparta.userservice.service.AuthService;
import com.sparta.userservice.service.GoogleOAuthService;
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

    public AuthController(GoogleOAuthService googleOAuthService, AuthService authService) {
        this.googleOAuthService = googleOAuthService;
        this.authService = authService;
    }

    // Google 로그인 URL 생성 및 반환
    @GetMapping("/google/login")
    public ResponseEntity<String> googleLogin() {
        log.info("확인");
        String googleAuthUrl = googleOAuthService.getGoogleAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", googleAuthUrl).build();
    }

    // Google 인증 후 Callback 처리
    @GetMapping("/google/callback")
    public ResponseEntity<TokenResponse> googleCallback(@RequestParam String code) {
        log.info("Callback hit with code: {}", code); // 디버깅 로그 추가
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
}