package com.sparta.payment.domain.user.service;

import com.sparta.payment.domain.user.dto.LoginRequestDto;
import com.sparta.payment.domain.user.dto.LoginResponseDto;
import com.sparta.payment.domain.user.entity.Role;
import com.sparta.payment.domain.user.entity.User;
import com.sparta.payment.domain.user.repository.UserRepository;
import com.sparta.payment.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void testSocialLogin_whenUserExists() {
        // Given
        String username = "testuser";
        String email = "testuser@example.com";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        User existingUser = new User(username, email, "SOCIAL_LOGIN", Role.USER);

        LoginRequestDto requestDto = new LoginRequestDto(username, email);

        // Mock 동작 정의
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(jwtTokenProvider.generateAccessToken(username)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(username)).thenReturn(refreshToken);

        // When
        LoginResponseDto responseDto = userService.socialLogin(requestDto);

        // Then
        assertEquals(accessToken, responseDto.getAccessToken());
        assertEquals(refreshToken, responseDto.getRefreshToken());

        // save() 호출을 허용, 그러나 new User()로 저장은 없어야 함
        verify(userRepository).save(existingUser);
    }

    @Test
    void testSocialLogin_whenUserDoesNotExist() {
        // Given
        String username = "newuser";
        String email = "newuser@example.com";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        User newUser = new User(username, email, "SOCIAL_LOGIN", Role.USER);

        LoginRequestDto requestDto = new LoginRequestDto(username, email);

        // Mock 동작 정의
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(jwtTokenProvider.generateAccessToken(username)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(username)).thenReturn(refreshToken);

        // When
        LoginResponseDto responseDto = userService.socialLogin(requestDto);

        // Then
        assertEquals(accessToken, responseDto.getAccessToken());
        assertEquals(refreshToken, responseDto.getRefreshToken());

        // save() 호출 횟수 검증
        verify(userRepository, times(1)).save(any(User.class)); // 정확히 1번만 호출
    }
}
