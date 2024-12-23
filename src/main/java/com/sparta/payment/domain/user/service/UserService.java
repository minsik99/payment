package com.sparta.payment.domain.user.service;

import com.sparta.payment.domain.user.dto.LoginRequestDto;
import com.sparta.payment.domain.user.dto.LoginResponseDto;
import com.sparta.payment.domain.user.entity.User;
import com.sparta.payment.domain.user.entity.Role;
import com.sparta.payment.domain.user.repository.UserRepository;
import com.sparta.payment.global.security.jwt.JwtTokenProvider;
import com.sparta.payment.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto socialLogin(LoginRequestDto requestDto) {
        // 사용자를 찾거나 새로 생성
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseGet(() -> {
                    // 새로운 사용자 생성 후 저장
                    User newUser = new User(requestDto.getUsername(), requestDto.getEmail(), "SOCIAL_LOGIN", Role.USER);
                    return userRepository.save(newUser);
                });

        // JWT Access 및 Refresh Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        // 새로 생성된 사용자가 아닌 경우에만 Refresh Token 업데이트 및 저장
        if (user.getId() != null && !refreshToken.equals(user.getRefreshToken())) {
            user.updateRefreshToken(refreshToken);
            userRepository.save(user);
        }

        return new LoginResponseDto(accessToken, refreshToken);
    }
}
