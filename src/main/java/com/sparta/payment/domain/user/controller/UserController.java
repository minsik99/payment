package com.sparta.payment.domain.user.controller;

import com.sparta.payment.domain.user.dto.LoginRequestDto;
import com.sparta.payment.domain.user.dto.LoginResponseDto;
import com.sparta.payment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/social-login")
    public LoginResponseDto socialLogin(@RequestBody LoginRequestDto requestDto) {
        return userService.socialLogin(requestDto);
    }
}
